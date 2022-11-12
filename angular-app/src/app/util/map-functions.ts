import {Route} from "../model/response/route";
declare let L;

export function drawPolyline(map, route: Route) {
  console.log("sjadosa");
  // if (this.polylineFound()){
  //   this.map.removeLayer(this.currentPolyline);
  // }
  let latLongs = [];
  route.locations.forEach(
    location => latLongs.push([location.lat, location.lon])
  )

  let currentPolyline = L.polyline(latLongs, {color: "red", weight:7}).addTo(map);
  map.fitBounds(currentPolyline.getBounds());
}
