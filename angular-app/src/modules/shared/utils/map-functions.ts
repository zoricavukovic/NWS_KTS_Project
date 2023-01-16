import {LngLat} from "../models/route/location";
import {PossibleRoute} from "../models/route/possible-routes";
import {DrivingLocation} from "../models/route/driving-location";
import {
  getActiveVehiclePhotoNameBasedOnType,
  getVehiclePhotoNameBasedOnType
} from "../models/vehicle/vehicle-type-info";
import {Driving} from "../models/driving/driving";
import {VehicleCurrentLocation} from "../models/vehicle/vehicle-current-location";
import {SearchingRoutesForm} from "../models/route/searching-routes-form";
import {Route} from "../models/route/route";
import {PossibleRoutesViaPoints} from "../models/route/possible-routes-via-points";


export function addMarker(map: google.maps.Map, markerCoordinates: google.maps.LatLng | google.maps.LatLngLiteral)
  :google.maps.Marker
{
  const customIcon = {
    url: './assets/images/marker-icon.png',
    anchor: new google.maps.Point(23,45),
    scaledSize: new google.maps.Size(45, 45)
  };
  map.setCenter(markerCoordinates);
  map.setZoom(16);
  return new google.maps.Marker(
    {
      position: markerCoordinates,
      map: map,
      title: 'Location',
      icon: customIcon
    });
}

export function drawAllMarkers(locations: DrivingLocation[] | undefined, map: google.maps.Map): google.maps.Marker[] {
  const markers: google.maps.Marker[] = [];
  locations.forEach(location => {
    const markerCoordinates: google.maps.LatLngLiteral = { lat: location.location.lat, lng: location.location.lon };
    const marker: google.maps.Marker = addMarker(map, markerCoordinates);
    if (location.index === 1 || location.index === locations.length){
      const infowindow = new google.maps.InfoWindow({
        content: `${location.location?.street} ${location.location?.number}`,
        ariaLabel: "Uluru",
      });
      google.maps.event.addListener(marker, 'click', function() {
        infowindow.open(map,marker);
      });
      infowindow.open(map,marker);
    }
    markers.push(marker);
  })
  return markers;
}


export function drawActiveRide(map: google.maps.Map, lngLatList: LngLat[] | undefined, driving: Driving): google.maps.Marker[] {
  const markers: google.maps.Marker[] = [];
  driving?.route?.locations.forEach(location => {
    const markerCoordinates: google.maps.LatLngLiteral = { lat: location.location.lat, lng: location.location.lon };
    const marker: google.maps.Marker = addMarker(map, markerCoordinates);
    if (location.index === 1 || location.index === driving?.route?.locations.length){
      // const infowindow = new google.maps.InfoWindow({
      //   content: `${location.location?.street} ${location.location?.number}`,
      //   ariaLabel: "Uluru",
      // });
      // google.maps.event.addListener(marker, 'click', function() {
      //   infowindow.open(map-page,marker);
      // });
      // infowindow.open(map-page,marker);
    }
    markers.push(marker);
  })
  return markers;
}

export function removeMarker(marker: google.maps.Marker) {
  marker.setMap(null);
}

export function removeAllMarkers(markers: SearchingRoutesForm[]): void{
  for (let i; i < markers.length; i++) {
    if (markers.at(i).marker){
      removeMarker(markers.at(i).marker);
    }
  }
}

export function removeAllMarkersFromList(markers: google.maps.Marker[]): void{
  markers.forEach(marker => removeMarker(marker));
}

export function polylineFound(polylines: google.maps.Polyline[]): boolean {

  return polylines !== null && polylines !== undefined;
}

export function removeAllPolyline(polylines: google.maps.Polyline[]): google.maps.Polyline[]{
  if (polylineFound(polylines)) {
    polylines.forEach(polyline => removeLine(polyline));
  }

  return [];
}

export function removeLine(polyline: google.maps.Polyline): void {
  polyline.setMap(null);
}

export function drawPolylineOnMapHaveRoute(map: google.maps.Map, route: Route | undefined): google.maps.Polyline | null {
  if (route){
    const latLongs: google.maps.LatLngLiteral[] = [];
    route.locations.forEach(locationIndex =>
      latLongs.push({lat:locationIndex.location.lat, lng:locationIndex.location.lon})
    );

    return drawPolylineOnMap(map, latLongs, "#283b50", 9);
  }
  return null;
}

export function drawPolylineWithLngLatArray(map: google.maps.Map, lngLatList: LngLat[]): google.maps.Polyline {
  const latLongs: google.maps.LatLngLiteral[] = [];
  lngLatList.forEach(lngLat =>
    latLongs.push({lat:lngLat[0], lng:lngLat[1]})
  );
  return drawPolylineOnMap(map, latLongs, "#283b50", 6);
}


export function drawPolylineOnMap(map: google.maps.Map, routeCoordinates: google.maps.LatLngLiteral[], color: string, weight: number)
  : google.maps.Polyline {
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

export function addLine(map: google.maps.Map, polyline: google.maps.Polyline): void {
  polyline.setMap(map);
}

export function getRouteCoordinates(route: PossibleRoute): google.maps.LatLngLiteral[] {
  const routeCoordinates: google.maps.LatLngLiteral[] = [];
  route.pointList.forEach(latLng =>
    routeCoordinates.push({ lat: latLng[0], lng: latLng[1] })
  );

  return routeCoordinates;
}

export function addCarMarker(map, vehicleStatus: VehicleCurrentLocation, currentLoggedUserId: number): google.maps.Marker {
  const customIcon: google.maps.Icon = vehicleStatus.inDrive?
    {
    url: getVehiclePhotoNameBasedOnType(vehicleStatus.type)
  } : {
    url: getActiveVehiclePhotoNameBasedOnType(vehicleStatus.type)
  };

  if (currentLoggedUserId === vehicleStatus?.driverId) {
    customIcon.anchor = new google.maps.Point(70, 70);
    customIcon.scaledSize = new google.maps.Size(70, 70);
  }
  else {
    customIcon.anchor = new google.maps.Point(50, 60);
    customIcon.scaledSize = new google.maps.Size(50, 50);
  }

  const marker: google.maps.Marker = new google.maps.Marker(
    {
      position: {lat:+vehicleStatus?.currentLocation?.lat, lng:+vehicleStatus?.currentLocation?.lon},
      map: map,
      title: 'Car',
      icon: customIcon
    });

  // marker.addListener("click", () => {
  //  let infoWindow = new google.maps.InfoWindow({
  //    content: "hellow<b>World</b>"
  //  });
  //  infoWindow.open(map-page, marker);
  // });

  return marker;
}

export function addCarMarkers(
  map: google.maps.Map,
  carMarkers: google.maps.Marker[],
  vehicleCurrentLocation: VehicleCurrentLocation[],
  currentLoggedUserId: number
): google.maps.Marker[] {
  if (map !== undefined){
    console.log(vehicleCurrentLocation);
    carMarkers.forEach(marker => removeMarker(marker));
    const markers: google.maps.Marker[] = [];
    vehicleCurrentLocation.forEach(currentVehicle => {
        markers.push(addCarMarker(map, currentVehicle, currentLoggedUserId));
    })

    return markers;
  }

  return carMarkers;
}

export function markCurrentPosition(map: google.maps.Map, vehicleCurrentLocation: VehicleCurrentLocation) {
  const customIcon: google.maps.Icon = {
      url: "./assets/images/pin_anim.svg",
      anchor: new google.maps.Point(60, 60),
      scaledSize: new google.maps.Size(60, 60)
  }

  const marker: google.maps.Marker = new google.maps.Marker(
    {
      position: {lat:vehicleCurrentLocation?.currentLocation?.lat, lng:vehicleCurrentLocation?.currentLocation?.lon},
      map: map,
      title: 'Car',
      icon: customIcon
    });
}


export function calculateMinutes(routePathIndexList: number[], possibleRoutesViaPoints: PossibleRoutesViaPoints[]): number {
  let minutes = 0;
  routePathIndexList.forEach(index => {
    minutes += possibleRoutesViaPoints
      .at(routePathIndexList.indexOf(index))
      .possibleRouteDTOList.at(index).timeInMin;
  });

  return minutes;
}

export function calculateDistance(routePathIndexList: number[], possibleRoutesViaPoints: PossibleRoutesViaPoints[]): number {
  let distance = 0;
  routePathIndexList.forEach(index => {
    distance += possibleRoutesViaPoints.at(routePathIndexList.indexOf(index))
      .possibleRouteDTOList.at(index).distance;
  });

  return distance;
}
