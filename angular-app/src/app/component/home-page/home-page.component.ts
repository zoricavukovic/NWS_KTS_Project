import { AfterViewInit, Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, ValidationErrors, Validators } from '@angular/forms';
import * as L from 'leaflet';
import { OpenStreetMapProvider } from 'leaflet-geosearch';

@Component({
  selector: 'home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css']
})
export class HomePageComponent implements AfterViewInit {

  private map;
  provider1 = new OpenStreetMapProvider();

  //inputStartPlace: string = "";
  filteredStartPlaces;
  startMarker;

  inputDestination: string = "";
  filteredDestinations;
  destinationMarker;

  autocompleteForm = new FormGroup({
    startDest: new FormControl(undefined, [this.requireMatch.bind(this)]),
    endDest: new FormControl(undefined, [this.requireMatch.bind(this)])
  });

  constructor() { }


  ngAfterViewInit(): void {
    this.initMap();
  }

  async initMap(){
  
    // this.map = L.map('map', {
    //   center: [ 39.8282, -98.5795 ],
    //   zoom: 3
    // });

    //this.map = L.map('map').setView({center: [51.505, -0.09], zoom:13});

    // const tiles = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    //   maxZoom: 18,
    //   minZoom: 3,
    //   attribution: '&copy; <a href="http://www.openstreetmap.org/copyright">OpenStreetMap</a>'
    // });

    // tiles.addTo(this.map);

    // const search = GeoSearch.GeoSearchControl({
    //   provider: new GeoSearch.OpenStreetMapProvider(),
    // });
    
    //this.map.addControl(search);


    this.map = L.map('map').setView([45.25167, 19.83694], 13);

    L.tileLayer('//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);
  }

  filterStartPlace(){
    this.filteredStartPlaces = this.filterPlaces(this.autocompleteForm.get("startDest").value);
    // console.log(this.filteredStartPlaces);
  }

  filterDestination(){
    this.filteredDestinations = this.filterPlaces(this.inputDestination);
    //console.log(this.filteredDestinations);
  }
    
  async filterPlaces(searchParam: string){
    const places = await this.provider1.search({ query: searchParam });
    return places;
  }


  requireMatch(control: FormControl): ValidationErrors | null {
    const selection: any = control.value;
    console.log(selection);
    
    // console.log(this.filteredStartPlaces)
    // if (selection !== null && this.filteredStartPlaces.value && this.filteredStartPlaces.value.indexOf(selection) < 0) {
    //   return { requireMatch: true };
    // }
    return null;
  } 

  onBlurStart(startPlace) {
    if (this.startMarker){
      this.map.removeLayer(this.startMarker);
    }
    const customIcon = L.icon({iconUrl: "../../../assets/images/circle.png", iconSize: [15, 15]})
    const markerOptions = {
      title: 'Pickup Location',
      clickable: true,
      icon: customIcon
    }
    this.startMarker = L.marker([startPlace.y, startPlace.x], markerOptions);
    this.startMarker.addTo(this.map);
  }

  onBlurDestination(destination) {
    if (this.destinationMarker){
      this.map.removeLayer(this.destinationMarker);
    }
    const customIcon = L.icon({iconUrl: "../../../assets/images/square.png", iconSize: [15, 15]})
    const markerOptions = {
      title: 'Pickup Location',
      clickable: true,
      icon: customIcon
    }
    this.destinationMarker = L.marker([destination.y, destination.x], markerOptions);
    this.destinationMarker.addTo(this.map);
  }

  // loseFocus(options): boolean {
  //   console.log(options.value);
  //   return options.value.filter(s => s.value.equals(this.autocompleteForm.get("startDest").value)).length === 0;
  //   return true;
  // }
}
