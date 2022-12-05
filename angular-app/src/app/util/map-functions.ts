import { Route } from '../model/route/route';
import {Location} from "../model/route/location";
import * as L from 'leaflet';
import {Vehicle} from "../model/vehicle/vehicle";
import {VehicleCurrentLocation} from "../model/vehicle/vehicle-current-location";

export function drawPolyline(map, route: Route): L.Polyline {
  let latLongs = [];
  route.locations.forEach(locationIndex =>
    latLongs.push([locationIndex.location.lat, locationIndex.location.lon])
  );

  let currentPolyline = L.polyline(latLongs, { color: 'red', weight: 7 }).addTo(
    map
  );
  map.fitBounds(currentPolyline.getBounds());

  return currentPolyline;
}

export function removeLayer(map, drawPolyline) {
  drawPolyline.forEach(polyline => map.removeLayer(polyline));
}

export function removeOneLayer(map, polyline) {
  map.removeLayer(polyline);
}

export function removeMarker(map: L.Map, marker: L.Marker) {
  map.removeLayer(marker);
}

export function drawPolylineOnMap(map: L.Map, latLongs: number[], color: string, weight: number): L.Polyline {
  const polyline: L.Polyline = L.polyline(latLongs, { color: color, weight: weight }).addTo(map);
  map.fitBounds(polyline.getBounds());

  return polyline;
}

export function drawPolylineOnMapWithoutClickEvent(map: L.Map, latLongs: number[]): L.Polyline {
  const polyline: L.Polyline = L.polyline(latLongs, { color: "#283b50", weight: 9 }).addTo(map);
  map.fitBounds(polyline.getBounds());

  return polyline;
}

export function refreshMap(map){
  map.off();
  map.eachLayer((layer) => {
      map.removeLayer(layer);
  })
}

export function removeSpecificPolyline(map, polyline: L.Polyline){
  map.off();
  map.eachLayer((layer) => {
    if (  layer === polyline) {
      map.removeLayer(layer);
    }
  })
}

export function addCarMarker(map, location: Location, iconName: string): L.Marker {
  const customIcon = L.icon({
    iconUrl: iconName,
    iconSize: [45, 45],
  });
  const markerOptions = {
    title: 'Car',
    clickable: true,
    icon: customIcon,
  };

  const carMarker = L.marker(
    [location?.lat, location?.lon],
    markerOptions
  );
  carMarker.addTo(map);

  return carMarker;
}

export function changeOrAddMarker(
  map, carMarkers: L.Marker[], vehicles: Vehicle[], vehicleCurrentLocation: VehicleCurrentLocation[]
): L.Marker[] {
  if (map !== undefined){
    carMarkers.forEach(marker => removeMarker(map, marker));
    let markers: L.Marker = [];
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
