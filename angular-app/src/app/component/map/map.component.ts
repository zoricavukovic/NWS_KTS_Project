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
export class MapComponent implements OnInit, AfterViewInit {
  map: L.Map;
  provider1 = new OpenStreetMapProvider();
  @ViewChild(DrivingDetailsComponent) private drivingDetailsComponent: DrivingDetailsComponent;
  homePage:boolean;

  constructor(private router: Router) { }

  ngOnInit(): void {
    if (this.map != undefined) { this.map.remove(); }
    this.initMap();
  }

  ngAfterViewInit(): void {
    this.drivingDetailsComponent.ngOnInit();
    // if (this.isHomeComponent(this.router?.url)){
    //   this.homePageComponent.ngOnInit()
    // }
    // else{
    //   this.drivingDetailsComponent.ngOnInit()
    // }
  }

  async initMap(){
    this.map = L.map('map').setView([45.25167, 19.83694], 13);

    L.tileLayer('//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { crossOrigin: true}).addTo(this.map);
  }

  private checkHomeComponent(url: string | undefined) {
    switch(url){
      case "/home-page": {
        this.homePage = true;
        break;
      }
      default:{
        this.homePage = false;
      }
    }
  }
}
