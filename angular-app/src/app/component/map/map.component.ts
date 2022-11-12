import {AfterViewInit, Component } from '@angular/core';
import * as L from 'leaflet';
import {OpenStreetMapProvider} from 'leaflet-geosearch';

@Component({
  selector: 'map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements AfterViewInit {

  map;
  provider1 = new OpenStreetMapProvider();

  constructor() { }

  ngAfterViewInit(): void {
    this.initMap();
  }

  async initMap(){
    this.map = L.map('map').setView([45.25167, 19.83694], 13);

    L.tileLayer('//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { crossOrigin: true}).addTo(this.map);
  }

}
