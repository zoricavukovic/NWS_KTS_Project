import {AfterViewInit, Component, Input, OnInit, ViewChild} from '@angular/core';
import * as L from 'leaflet';
import {OpenStreetMapProvider} from 'leaflet-geosearch';
import {Route} from "../../model/response/route";
import {DrivingDetailsComponent} from "../driving/driving-details/driving-details.component";
import {PossibleRoute} from "../../model/response/possible-routes";

@Component({
  selector: 'map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, AfterViewInit {
  map;
  provider1 = new OpenStreetMapProvider();
  @ViewChild(DrivingDetailsComponent) private drivingDetailsComponent: DrivingDetailsComponent;
  constructor() { }
  ngOnInit(): void {
    this.initMap();
  }

  ngAfterViewInit(): void {
    this.drivingDetailsComponent.ngOnInit();

  }

  async initMap(){
    this.map = L.map('map').setView([45.25167, 19.83694], 13);

    L.tileLayer('//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { crossOrigin: true}).addTo(this.map);
  }

  // drawPolyline(route: Route) {
  //   console.log("sjadosa");
  //   // if (this.polylineFound()){
  //   //   this.map.removeLayer(this.currentPolyline);
  //   // }
  //   let latLongs = [];
  //   route.locations.forEach(
  //     location => latLongs.push([location.lat, location.lon])
  //   )
  //
  //   let currentPolyline = L.polyline(latLongs, {color: "red", weight:7}).addTo(this.map);
  //   this.map.fitBounds(currentPolyline.getBounds());
  // }

}
