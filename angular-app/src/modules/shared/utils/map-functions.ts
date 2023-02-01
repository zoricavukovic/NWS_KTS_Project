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
  console.log(destination);
  console.log(source);
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

  console.log(vehicle);
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

export function removeAllMarkers(markers: SearchingRoutesForm[]): void {
  for (let i; i < markers.length; i++) {
    if (markers.at(i).marker) {
      removeMarker(markers.at(i).marker);
    }
  }
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

export function polylineFound(polylines: google.maps.Polyline[]): boolean {
  return polylines !== null && polylines !== undefined;
}

export function removeAllPolyline(
  polylines: google.maps.Polyline[]
): google.maps.Polyline[] {
  if (polylineFound(polylines)) {
    polylines.forEach(polyline => removeLine(polyline));
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
  storeDriving: DrivingNotification
) {
  const customIcon: google.maps.Icon =
    vehicleStatus.id === storeDriving?.vehicleId &&
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
  storeDriving: DrivingNotification
): google.maps.Marker {
  const customIcon = getMarkerIcon(
    vehicleStatus,
    currentLoggedUserId,
    storeDriving
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
  isHomePage: boolean
): google.maps.Marker {
  if (map !== undefined) {
    if (!marker.getVisible() && isHomePage) {
      marker.setVisible(true);
    }

    marker.setIcon(
      getMarkerIcon(vehicleCurrentLocation, currentLoggedUserId, storeDriving)
    );
    marker.setTitle(getTitle(vehicleCurrentLocation, storeDriving));

    console.log(vehicleCurrentLocation);
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
