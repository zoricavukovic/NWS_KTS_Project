import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import * as L from 'leaflet';
import { OpenStreetMapProvider } from 'leaflet-geosearch';
import { DrivingDetailsComponent } from '../driving/driving-details/driving-details.component';
import { refreshMap } from '../../util/map-functions';
import { HomePageComponent } from '../home/home-page/home-page.component';

@Component({
  selector: 'map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css'],
})
export class MapComponent implements OnInit, OnDestroy {
  protected map: L.Map;
  provider1 = new OpenStreetMapProvider();
  @ViewChild(DrivingDetailsComponent)
  private drivingDetailsComponent: DrivingDetailsComponent;

  constructor() {}

  ngOnInit(): void {
    /*console.log(this.map);
    if (this.map != undefined) {
      refreshMap(this.map);
    } else {
      this.initMap();
    }
    console.log(this.map);*/
    this.initMap();
  }

  async initMap() {
    this.map = L.map('map').setView([45.25167, 19.83694], 13);

    L.tileLayer('//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      crossOrigin: true,
    }).addTo(this.map);
  }

  ngOnDestroy(): void {
    refreshMap(this.map);
    this.map.remove();
  }
}
