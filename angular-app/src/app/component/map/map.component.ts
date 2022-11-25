import {AfterViewInit, Component, OnDestroy, OnInit, ViewChild} from '@angular/core';
import * as L from 'leaflet';
import {OpenStreetMapProvider} from 'leaflet-geosearch';
import {DrivingDetailsComponent} from "../driving/driving-details/driving-details.component";
import {refreshMap} from "../../util/map-functions";
import {Router, UrlSegment} from '@angular/router';
import {ActivatedRoute} from "@angular/router";
import {HomePageComponent} from "../home/home-page/home-page.component";


import "leaflet/dist/leaflet.css";
import "esri-leaflet-geocoder/dist/esri-leaflet-geocoder";
import * as ELG from "esri-leaflet-geocoder";

delete L.Icon.Default.prototype._getIconUrl;


@Component({
  selector: 'map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css']
})
export class MapComponent implements OnInit, OnDestroy, AfterViewInit {
  map: L.Map;
  provider1 = new OpenStreetMapProvider();
  @ViewChild(DrivingDetailsComponent) private drivingDetailsComponent: DrivingDetailsComponent;
  @ViewChild(HomePageComponent) private homePageComponent: HomePageComponent;

  marker;
  tekst: UrlSegment[];
  constructor(public router: Router, public actRoute: ActivatedRoute) {
    this.actRoute.url.subscribe(url =>{
      this.tekst = url;
      console.log(this.tekst.at(0).path);

    });
  }

  ngOnInit(): void {
    if (this.map !== undefined) { this.map = this.map.remove(); }
    // this.map?.off();
    // this.map.remove();
    this.initMap();
  }

  ngAfterViewInit(): void {

    // if (this.router.url.startsWith("/driving-details")){
    //   this.drivingDetailsComponent.ngOnInit();
    // }
    // else{
    //   this.homePageComponent.ngOnInit();
    // }
    // refreshMap(this.map);

  }

  async initMap(){
    this.map = L.map('map').setView([45.25167, 19.83694], 13);
    L.control.zoom({
      position: 'topright'
    }).addTo(this.map);
    L.tileLayer('//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { crossOrigin: true}).addTo(this.map);

  }

  ngOnDestroy(): void {
    // refreshMap(this.map);
    if (this.map !== undefined) { this.map = this.map.remove(); }
    // refreshMap(this.map);
  }
}
