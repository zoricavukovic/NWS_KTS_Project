import { LngLat, Location } from '../models/route/location';
import { PossibleRoute } from '../models/route/possible-routes';
import { DrivingLocation } from '../models/route/driving-location';
import {
  getActiveVehiclePhotoNameBasedOnType,
  getMyVehicle,
  getVehiclePhotoNameBasedOnType,
} from '../models/vehicle/vehicle-type-info';
import { Driving } from '../models/driving/driving';
import { VehicleCurrentLocation } from '../models/vehicle/vehicle-current-location';
import { SearchingRoutesForm } from '../models/route/searching-routes-form';
import { Route } from '../models/route/route';
import { PossibleRoutesViaPoints } from '../models/route/possible-routes-via-points';
import { CurrentVehiclePosition } from '../models/vehicle/current-vehicle-position';
import { Driver } from '../models/user/driver';
import { UpdateOnlyMinutesStatus } from '../actions/driving-notification.action';
import { Store } from '@ngxs/store';
import { DrivingNotification } from '../models/notification/driving-notification';
import {FormGroup} from "@angular/forms";
import {Address} from "ngx-google-places-autocomplete/objects/address";

export function createLocationFromAutocomplete(address: Address, index: number, rideRequestForm: FormGroup): Location {
  let houseNumber = '';
  let city = '';
  let street = '';
  let zipCode = '';
  for (let i = 0; i < address.address_components.length; i++) {
    const mapAddress = address.address_components[i];
    if (mapAddress.long_name !== '') {
      if (mapAddress.types[0] === 'street_number') {
        houseNumber = mapAddress.long_name;
      }
      if (mapAddress.types[0] === 'route') {
        street = mapAddress.long_name;
      }

      if (mapAddress.types[0] === 'locality') {
        city = mapAddress.long_name;
      }
      if (mapAddress.types[0] === 'postal_code') {
        zipCode = mapAddress.long_name;
      }
    }
  }
  const lng: number = address.geometry.location.lng();
  const lat: number = address.geometry.location.lat();
  const location: Location = {
    city: city,
    lon: lng,
    lat: lat,
    street: street,
    number: houseNumber,
    zipCode: zipCode,
  };
  rideRequestForm.get('searchingRoutesForm').value.at(index).location = location;

  return location;
}

export function addMarker(
  map: google.maps.Map,
  markerCoordinates: google.maps.LatLng | google.maps.LatLngLiteral
): google.maps.Marker {
  const customIcon = {
    url: './assets/images/marker-icon.png',
    anchor: new google.maps.Point(23, 45),
    scaledSize: new google.maps.Size(45, 45),
  };
  map.setCenter(markerCoordinates);
  map.setZoom(16);
  return new google.maps.Marker({
    position: markerCoordinates,
    map: map,
    title: 'Location',
    icon: customIcon,
  });
}

export function addMarkerWithCustomIcon(lat: number, lng: number, index: number, map: google.maps.Map, searchingForm)
:google.maps.Marker {
  const marker: google.maps.Marker = addMarker(map, {
    lat: lat,
    lng: lng,
  });
  marker.setIcon(getIconUrl(index, searchingForm));

  return marker;
}

export function addMarkerWithLonLat(lat: number, lon: number, map: google.maps.Map, index: number, listToCheckIndex): google.maps.Marker {
  const markerCoordinates: google.maps.LatLngLiteral = { lat: lat, lng: lon };
  const marker: google.maps.Marker = addMarker(map, markerCoordinates);
  marker.setIcon(getIconUrl(index, listToCheckIndex));
  return marker;
}

export function drawAllMarkers(
  locations: DrivingLocation[] | undefined,
  map: google.maps.Map
): google.maps.Marker[] {
  const markers: google.maps.Marker[] = [];
  locations.forEach(location => {
    const markerCoordinates: google.maps.LatLngLiteral = {
      lat: location.location.lat,
      lng: location.location.lon,
    };
    const marker: google.maps.Marker = addMarker(map, markerCoordinates);
    if (location.index === 1 || location.index === locations.length) {
      const infowindow = new google.maps.InfoWindow({
        content: `${location.location?.street} ${location.location?.number}`,
        ariaLabel: 'Uluru',
      });
      google.maps.event.addListener(marker, 'click', function () {
        infowindow.open(map, marker);
      });
      infowindow.open(map, marker);
    }
    markers.push(marker);
  });
  return markers;
}

function calculateMinutesToDestination(
  vehicle: CurrentVehiclePosition,
  directionService: google.maps.DirectionsService,
  currentLocation: Location,
  endLocation: Location,
  storedDrivingNotification: DrivingNotification,
  store: Store
): void {
  const source = {
    lat: currentLocation.lat,
    lng: currentLocation.lon,
  };

  const destination = {
    lat: endLocation.lat,
    lng: endLocation.lon,
  };

  const request = {
    origin: source,
    destination: destination,
    travelMode: google.maps.TravelMode.DRIVING,
  };

  directionService.route(request, (response, status) => {
    if (status === google.maps.DirectionsStatus.OK) {
      const distanceInfo = response.routes[0].legs[0];
      vehicle.vehicleCurrentLocation.timeToDestination -=
        distanceInfo.duration.value / 60;

      store
        .dispatch(
          new UpdateOnlyMinutesStatus({
            minutes: distanceInfo.duration.value / 60,
          })
        )
        .subscribe(() => {
          console.log(storedDrivingNotification);
        });
    }
  });
}

export function calculateTimeToDestination(
  vehicle: CurrentVehiclePosition,
  route: Route,
  directionService: google.maps.DirectionsService,
  store: Store,
  storedDrivingNotification: DrivingNotification
) {
  calculateMinutesToDestination(
    vehicle,
    directionService,
    vehicle.vehicleCurrentLocation.currentLocation,
    route.locations.at(route.locations.length - 1).location,
    storedDrivingNotification,
    store
  );
}

export function drawActiveRide(
  map: google.maps.Map,
  driving: Driving,
  driver: Driver,
  vehicle: CurrentVehiclePosition,
  index: number,
  directionService: google.maps.DirectionsService,
  store: Store,
  isAdmin: boolean,
  storedDrivingNotification: DrivingNotification
): google.maps.Marker[] {
  const markers: google.maps.Marker[] = [];
  if (vehicle && driving.active) {
    visibleMarker(vehicle.marker);
    if (!isAdmin) {
      calculateTimeToDestination(
        vehicle,
        driving?.route,
        directionService,
        store,
        storedDrivingNotification
      );
    }
  }

  driving?.route?.locations.forEach(location => {
    const markerCoordinates: google.maps.LatLngLiteral = {
      lat: location.location.lat,
      lng: location.location.lon,
    };
    const marker: google.maps.Marker = addMarker(map, markerCoordinates);
    marker.setIcon({
      url: './assets/images/marker-icon.png',
      anchor: new google.maps.Point(15, 30),
      scaledSize: new google.maps.Size(30, 30),
    });

    markers.push(marker);
  });

  return markers;
}

export function removeMarker(marker: google.maps.Marker) {
  marker.setMap(null);
}

export function hideMarker(marker: google.maps.Marker) {
  marker.setVisible(false);
}

export function visibleMarker(marker: google.maps.Marker) {
  marker.setVisible(true);
}

export function removeMarkerAndAllPolyline(drawPolylineList: google.maps.Polyline[], formField): google.maps.Polyline[] {
  if (formField.value?.marker) {
    removeMarker(formField.value.marker);
    return removeAllPolyline(drawPolylineList);
  }
  return drawPolylineList;
}


export function hideAllMarkers(markers: SearchingRoutesForm[]): void {
  for (let i; i < markers.length; i++) {
    if (markers.at(i).marker) {
      hideMarker(markers.at(i).marker);
    }
  }
}

export function removeAllMarkersFromList(markers: google.maps.Marker[]): void {
  markers.forEach(marker => removeMarker(marker));
}

export function polylineFound(polyline: google.maps.Polyline[]): boolean {
  return polyline !== null && polyline !== undefined;
}

export function removeAllPolyline(polyline: google.maps.Polyline[]): google.maps.Polyline[] {
  if (polylineFound(polyline)) {
    polyline.forEach(polyline => removeLine(polyline));
  }

  return [];
}

export function removeLine(polyline: google.maps.Polyline): void {
  polyline.setMap(null);
}

export function drawPolylineWithLngLatArray(
  map: google.maps.Map,
  lngLatList: LngLat[]
): google.maps.Polyline {
  const latLongs: google.maps.LatLngLiteral[] = [];
  lngLatList.forEach(lngLat =>
    latLongs.push({ lat: lngLat[0], lng: lngLat[1] })
  );
  return drawPolylineOnMap(map, latLongs, '#283b50', 6);
}

export function drawPolylineOnMap(
  map: google.maps.Map,
  routeCoordinates: google.maps.LatLngLiteral[],
  color: string,
  weight: number
): google.maps.Polyline {
  const polyline: google.maps.Polyline = new google.maps.Polyline({
    path: routeCoordinates,
    strokeColor: color,
    strokeOpacity: 1.0,
    strokeWeight: weight,
  });
  const bounds = new google.maps.LatLngBounds();
  for (let i = 0; i < routeCoordinates.length; i++) {
    bounds.extend(routeCoordinates[i]);
  }
  map.fitBounds(bounds);
  addLine(map, polyline);

  return polyline;
}

export function drawPolylineWithClickOption(
  indexOfSelectedPath: number,
  indexOfRouteInPossibleRoutes: number,
  latLongs: google.maps.LatLngLiteral[],
  drawPolylineList: google.maps.Polyline[],
  map: google.maps.Map,
  rideRequestForm: FormGroup
): void {
  const color: string = indexOfSelectedPath === 0 ? '#283b50' : '#cdd1d3';
  const weight: number = indexOfSelectedPath === 0 ? 9 : 7;
  const polyline: google.maps.Polyline = drawPolylineOnMap(map, latLongs, color, weight);
  polyline.setOptions({ clickable: true });
  drawPolylineList[indexOfSelectedPath] = polyline;

  google.maps.event.addListener(polyline, 'click', function () {
    (rideRequestForm.get('routePathIndex').value)[indexOfRouteInPossibleRoutes] = indexOfSelectedPath;
    drawPolylineList.forEach(p => {
      p.setOptions({
        strokeColor: '#cdd1d3',
        strokeWeight: 7,
      });

      polyline.setOptions({
        strokeColor: '#283b50',
        strokeWeight: 9,
      });
    });
  });
}

export function addLine(
  map: google.maps.Map,
  polyline: google.maps.Polyline
): void {
  polyline.setMap(map);
}

export function getRouteCoordinates(
  route: PossibleRoute
): google.maps.LatLngLiteral[] {
  const routeCoordinates: google.maps.LatLngLiteral[] = [];
  route.pointList.forEach(latLng =>
    routeCoordinates.push({ lat: latLng[0], lng: latLng[1] })
  );

  return routeCoordinates;
}

function getMarkerIcon(
  vehicleStatus: VehicleCurrentLocation,
  currentLoggedUserId: number,
  storeDriving: DrivingNotification,
  isRegularUser: boolean
) {
  const customIcon: google.maps.Icon =
    vehicleStatus.id === storeDriving?.vehicleId && isRegularUser &&
    (storeDriving?.drivingStatus === 'ON_WAY_TO_DEPARTURE' ||
      (storeDriving?.drivingStatus === 'ACCEPTED' && storeDriving?.active))
      ? {
          url: getMyVehicle(vehicleStatus.type),
        }
      : vehicleStatus.inDrive
      ? {
          url: getVehiclePhotoNameBasedOnType(vehicleStatus.type),
        }
      : {
          url: getActiveVehiclePhotoNameBasedOnType(vehicleStatus.type),
        };

  if (currentLoggedUserId === vehicleStatus?.driverId) {
    customIcon.anchor = new google.maps.Point(35, 30);
    customIcon.scaledSize = new google.maps.Size(70, 70);
  } else {
    customIcon.anchor = new google.maps.Point(35, 30);
    customIcon.scaledSize = new google.maps.Size(50, 50);
  }

  return customIcon;
}

export function addCarMarker(
  map,
  vehicleStatus: VehicleCurrentLocation,
  currentLoggedUserId: number,
  storeDriving: DrivingNotification,
  isRegularUser: boolean
): google.maps.Marker {
  const customIcon = getMarkerIcon(
    vehicleStatus,
    currentLoggedUserId,
    storeDriving,
    isRegularUser
  );

  return new google.maps.Marker({
    position: {
      lat: +vehicleStatus?.currentLocation?.lat,
      lng: +vehicleStatus?.currentLocation?.lon,
    },
    map: map,
    title: 'Car',
    icon: customIcon,
  });
}

function getTitle(
  vehicleCurrentLocation: VehicleCurrentLocation,
  storedDriving: DrivingNotification
) {
  return vehicleCurrentLocation.id === storedDriving?.vehicleId &&
    (storedDriving?.drivingStatus === 'ON_WAY_TO_DEPARTURE' ||
      (storedDriving?.drivingStatus === 'ACCEPTED' && storedDriving?.active))
    ? 'My driver'
    : 'Car';
}

export function updateVehiclePosition(
  storeDriving: DrivingNotification,
  map: google.maps.Map,
  marker: google.maps.Marker,
  vehicleCurrentLocation: VehicleCurrentLocation,
  currentLoggedUserId: number,
  isHomePage: boolean,
  isRegularUser: boolean
): google.maps.Marker {
  if (map !== undefined) {
    if (!marker.getVisible() && isHomePage) {
      marker.setVisible(true);
    }

    marker.setIcon(
      getMarkerIcon(vehicleCurrentLocation, currentLoggedUserId, storeDriving, isRegularUser)
    );
    marker.setTitle(getTitle(vehicleCurrentLocation, storeDriving));

    if (vehicleCurrentLocation.inDrive) {
      marker.setPosition({
        lat: vehicleCurrentLocation.currentLocation.lat,
        lng: vehicleCurrentLocation.currentLocation.lon,
      });
      return marker;
    }

    return marker;
  }

  return marker;
}

export function calculateMinutes(
  routePathIndexList: number[],
  possibleRoutesViaPoints: PossibleRoutesViaPoints[]
): number {
  let minutes = 0;
  routePathIndexList.forEach(index => {
    minutes += possibleRoutesViaPoints
      .at(routePathIndexList.indexOf(index))
      .possibleRouteDTOList.at(index).timeInMin;
  });

  return minutes;
}

export function calculateDistance(
  routePathIndexList: number[],
  possibleRoutesViaPoints: PossibleRoutesViaPoints[]
): number {
  let distance = 0;
  routePathIndexList.forEach(index => {
    distance += possibleRoutesViaPoints
      .at(routePathIndexList.indexOf(index))
      .possibleRouteDTOList.at(index).distance;
  });

  return distance;
}

export function getIconUrl(index: number, listForCheckingIndex): string {
  switch (index) {
    case 0:
      return '../../../assets/images/startMarker.png';
    case listForCheckingIndex.length - 1:
      return '../../../assets/images/endMarker.png';
    default:
      return '../../../assets/images/viaMarker.png';
  }
}
