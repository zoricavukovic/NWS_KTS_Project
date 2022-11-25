import { Route } from '../model/route/route';
import {Location} from "../model/route/location";
declare let L;

export function drawPolyline(map, route: Route) {
  console.log(route);
  let latLongs = [];
  route.locations.forEach(locationIndex =>
    latLongs.push([locationIndex.location.lat, locationIndex.location.lon])
  );

  let currentPolyline = L.polyline(latLongs, { color: 'red', weight: 7 }).addTo(
    map
  );
  map.fitBounds(currentPolyline.getBounds());
}

export function removeLayer(map, drawPolyline) {
  drawPolyline.forEach(polyline => map.removeLayer(polyline));
}

export function removeOneLayer(map, polyline) {
  map.removeLayer(polyline);
}

export function removeMarker(map, marker) {
  map.removeLayer(marker);
}

export function drawPolylineOnMap(map, latLongs, color, polylineList) {
  let polyline = L.polyline(latLongs, { color: color, weight: 7 }).addTo(map);
  polylineList.push(polyline);
  map.fitBounds(polyline.getBounds());
}

export function refreshMap(map){
  map.off();
  map.remove();
}

export function addCarMarker(map, location: Location){
  const customIcon = L.icon({
    iconUrl: '/assets/images/car.png',
    iconSize: [45, 45],
  });
  const markerOptions = {
    title: 'Car',
    clickable: true,
    icon: customIcon,
  };

  L.marker(
    [location?.lat, location?.lon],
    markerOptions
  ).addTo(map);
  map.panBy(L.point(location?.lat, location?.lon));
}
