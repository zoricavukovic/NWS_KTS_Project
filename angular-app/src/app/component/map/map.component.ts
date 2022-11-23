import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import * as L from 'leaflet';
import {OpenStreetMapProvider} from 'leaflet-geosearch';
import {DrivingDetailsComponent} from "../driving/driving-details/driving-details.component";
import {Router} from "@angular/router";

@Component({
  selector: 'map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit {
  map: L.Map;
  provider1 = new OpenStreetMapProvider();
  @ViewChild(DrivingDetailsComponent) private drivingDetailsComponent: DrivingDetailsComponent;

  constructor() { }

  ngOnInit(): void {
    if (this.map != undefined) { this.map.remove(); }
    this.initMap();
  }

  async initMap(){
    this.map = L.map('map').setView([45.25167, 19.83694], 13);

    L.tileLayer('//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { crossOrigin: true}).addTo(this.map);
  }
}
