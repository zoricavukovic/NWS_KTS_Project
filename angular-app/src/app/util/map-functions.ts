import {Route} from "../model/response/route";
declare let L;

export function drawPolyline(map, route: Route) {

  let latLongs = [];
  route.locations.forEach(
    location => latLongs.push([location.lat, location.lon])
  )

  let currentPolyline = L.polyline(latLongs, {color: "red", weight:7}).addTo(map);
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

export function drawPolylineOnMap(map, latLongs, color, polylineList){
  let polyline = L.polyline(latLongs, {color: color, weight: 7}).addTo(map);
  polylineList.push(polyline);
  map.fitBounds(polyline.getBounds());
}