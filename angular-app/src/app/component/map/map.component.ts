import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { DrivingDetailsComponent } from '../driving/driving-details-components/driving-details/driving-details.component';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { HomePageComponent } from '../home/home-page/home-page.component';

@Component({
  selector: 'map',
  templateUrl: './map.component.html',
  styleUrls: ['./map.component.css'],
})
export class MapComponent implements OnInit, OnDestroy {

  @ViewChild(DrivingDetailsComponent) private drivingDetailsComponent: DrivingDetailsComponent;
  @ViewChild(HomePageComponent) private homePageComponent: HomePageComponent;

  map: google.maps.Map;
  center: google.maps.LatLngLiteral = {lat: 45.25167, lng: 19.83694};
  zoom= 13;

  constructor(public router: Router, public actRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.initMap();
  }

  ngOnDestroy(): void {

    if (this.map !== undefined) {
      this.map = null;
    }
  }

  private initMap(): void {
    this.map = new google.maps.Map(document.getElementById("map") as HTMLElement, {
      center: this.center,
      mapId: '18f859a923044aa6',
      mapTypeId:google.maps.MapTypeId[Symbol.hasInstance],
      zoom: this.zoom,
      zoomControl: true,
      zoomControlOptions: {
        position: google.maps.ControlPosition.TOP_RIGHT,
      },
      streetViewControl: false
    });
  }
}
