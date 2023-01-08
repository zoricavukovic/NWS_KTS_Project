import { Route } from '../model/route/route';
import {LngLat, Location} from "../model/route/location";
import {Vehicle} from "../model/vehicle/vehicle";
import {VehicleCurrentLocation} from "../model/vehicle/vehicle-current-location";
import {PossibleRoute} from "../model/route/possible-routes";
import {SearchingRoutesForm} from "../model/route/searching-routes-form";
import {DrivingLocation} from "../model/route/driving-location";

export function addMarker(map: google.maps.Map, markerCoordinates: google.maps.LatLng | google.maps.LatLngLiteral)
  :google.maps.Marker
{
  map.setCenter(markerCoordinates);
  map.setZoom(16);
  return new google.maps.Marker(
    {
      position: markerCoordinates,
      map: map,
      title: 'Location'
    });
}

export function drawAllMarkers(locations: DrivingLocation[] | undefined, map: google.maps.Map): google.maps.Marker[] {
  const markers: google.maps.Marker[] = [];
  locations.forEach(location => {
    const markerCoordinates: google.maps.LatLngLiteral = { lat: location.location.lat, lng: location.location.lon };
    markers.push(addMarker(map, markerCoordinates));
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
    let latLongs: google.maps.LatLngLiteral[] = [];
    route.locations.forEach(locationIndex =>
      latLongs.push({lat:locationIndex.location.lat, lng:locationIndex.location.lon})
    );

    return drawPolylineOnMap(map, latLongs, "#283b50", 9);
  }
  return null;
}

export function drawPolylineWithLngLatArray(map: google.maps.Map, lngLatList: LngLat[]): google.maps.Polyline {
  let latLongs: google.maps.LatLngLiteral[] = [];
  lngLatList.forEach(lngLat =>
    latLongs.push({lat:lngLat[0], lng:lngLat[1]})
  );
  return drawPolylineOnMap(map, latLongs, "#283b50", 9);
}


export function drawPolylineOnMap(map: google.maps.Map, routeCoordinates: google.maps.LatLngLiteral[], color: string, weight: number)
  : google.maps.Polyline {
  const polyline: google.maps.Polyline = new google.maps.Polyline({
    path: routeCoordinates,
    strokeColor: color,
    strokeOpacity: 1.0,
    strokeWeight: weight,
  });
  console.log(routeCoordinates);
  let bounds = new google.maps.LatLngBounds();
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
  let routeCoordinates: google.maps.LatLngLiteral[] = [];
  route.pointList.forEach(latLng =>
    routeCoordinates.push({ lat: latLng[0], lng: latLng[1] })
  );

  return routeCoordinates;
}

export function addCarMarker(map, location: Location, iconName: string): google.maps.Marker {
  const customIcon = {
    url: iconName,
    scaledSize: new google.maps.Size(45, 45)
  };

  return new google.maps.Marker(
    {
      position: {lat:location?.lat, lng:location?.lon},
      map: map,
      title: 'Car',
      icon: customIcon
    });
}

export function addCarMarkers(
  map: google.maps.Map, carMarkers: google.maps.Marker[], vehicles: Vehicle[], vehicleCurrentLocation: VehicleCurrentLocation[]
): google.maps.Marker[] {
  if (map !== undefined){
    carMarkers.forEach(marker => removeMarker(marker));
    let markers: google.maps.Marker[] = [];
    vehicleCurrentLocation.forEach(currentVehicle => {
      markers.push(currentVehicle.inDrive === true ?
        addCarMarker(map, currentVehicle?.currentLocation, '/assets/images/car.png'):
        addCarMarker(map, currentVehicle?.currentLocation, '/assets/images/car-unactive.png')
      );
    })

    return markers;
  }

  return carMarkers;
}
