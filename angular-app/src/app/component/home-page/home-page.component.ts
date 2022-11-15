import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, ValidationErrors } from '@angular/forms';
import * as L from 'leaflet';
import { OpenStreetMapProvider } from 'leaflet-geosearch';
import { SearchingRoutesForm } from '../../model/searching-routes-form';
import { Location } from '../../model/response/location';
import { RouteService } from '../../service/route.service';
import { LocationsForRoutesRequest } from '../../model/request/locations-for-routes-request';
import { PossibleRoute } from '../../model/response/possible-routes';
import { User } from '../../model/response/user/user';
import { Subscription } from 'rxjs';
import { AuthService } from '../../service/auth.service';

@Component({
  selector: 'home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements OnInit, OnDestroy, AfterViewInit {
  private map;
  currentUser: User;
  provider1 = new OpenStreetMapProvider();

  maxNumberOfLocations = 7;

  possibleRoutes: PossibleRoute[] = [];
  currentPolyline;
  searchingRoutesForm: SearchingRoutesForm[] = [];
  /* autocompleteForm = new FormGroup({
    startDest: new FormControl(undefined, [this.requireMatch.bind(this)]),
    endDest: new FormControl(undefined, [this.requireMatch.bind(this)]),
  });*/

  rgbDeepBlue: number[] = [44, 75, 97];
  private authSubscription: Subscription;
  private routeSubscription: Subscription;

  constructor(
    private routeService: RouteService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.searchingRoutesForm.push(new SearchingRoutesForm());
    this.searchingRoutesForm.push(new SearchingRoutesForm());
    this.authSubscription = this.authService
      .getCurrentUser()
      .subscribe(user => (this.currentUser = user));
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  async initMap() {
    this.map = L.map('map').setView([45.25167, 19.83694], 13);

    L.tileLayer('//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      crossOrigin: true,
    }).addTo(this.map);
  }

  addOneMoreLocation() {
    this.searchingRoutesForm.splice(
      this.searchingRoutesForm.length - 1,
      0,
      new SearchingRoutesForm()
    );
  }

  deleteOneLocation(index: number) {
    this.searchingRoutesForm.splice(index, 1);
  }

  async filterPlaces(searchParam: string) {
    return await this.provider1.search({ query: searchParam });
  }

  /*requireMatch(control: FormControl): ValidationErrors | null {
    const selection: any = control.value; //??
    console.log(selection);

    // console.log(this.filteredStartPlaces)
    // if (selection !== null && this.filteredStartPlaces.value && this.filteredStartPlaces.value.indexOf(selection) < 0) {
    //   return { requireMatch: true };
    // }
    return null;
  }*/

  chooseMarker(index: number, place) {
    if (this.searchingRoutesForm.at(index).marker) {
      this.map.removeLayer(this.searchingRoutesForm.at(index).marker);
    }
    if (this.polylineFound()) {
      this.map.removeLayer(this.currentPolyline);
    }

    const customIcon = L.icon({
      iconUrl: this.getIconUrl(index),
      iconSize: [30, 30],
    });
    const markerOptions = {
      title: 'Location',
      clickable: true,
      icon: customIcon,
    };
    this.searchingRoutesForm.at(index).marker = L.marker(
      [place.y, place.x],
      markerOptions
    );
    this.searchingRoutesForm.at(index).marker.addTo(this.map);

    this.createLocation(place, index);
  }

  getIconName(index: number): string {
    switch (index) {
      case 1:
        return 'looks_one';
      case 2:
        return 'looks_two';
      default:
        return 'looks_' + index;
    }
  }

  getFilteredPlaces(index: number) {
    return this.searchingRoutesForm.at(index).filteredPlaces;
  }

  filterPlace(index: number) {
    this.searchingRoutesForm.at(index).filteredPlaces = this.filterPlaces(
      this.searchingRoutesForm.at(index).inputPlace
    );
  }

  getPossibleRoutes() {
    const locationsForCreateRoutes: Location[] = [];
    this.searchingRoutesForm.forEach(searchingRoutesLocation =>
      locationsForCreateRoutes.push(searchingRoutesLocation.location)
    );

    this.routeSubscription = this.routeService
      .getPossibleRoutes(
        new LocationsForRoutesRequest(locationsForCreateRoutes)
      )
      .subscribe(
        res => {
          this.possibleRoutes = res;
          if (res.length > 0) {
            this.changeCurrentRoute(res.at(0), 0);
          }
        },
        error => {
          console.log(error);
        }
      );
  }

  changeCurrentRoute(route: PossibleRoute, index: number) {
    if (this.polylineFound()) {
      this.map.removeLayer(this.currentPolyline);
    }
    const latLongs = [];
    route.pointList.forEach(latLng => latLongs.push([latLng[0], latLng[1]]));
    const color: string =
      'rgb(' +
      this.incrementShadeOfColor(index, 0) +
      ', ' +
      this.incrementShadeOfColor(index, 1) +
      ', ' +
      this.incrementShadeOfColor(index, 2) +
      ')';

    this.currentPolyline = L.polyline(latLongs, {
      color: color,
      weight: 7,
    }).addTo(this.map);
    this.map.fitBounds(this.currentPolyline.getBounds());
  }

  private polylineFound() {
    return this.currentPolyline !== null && this.currentPolyline !== undefined;
  }

  private incrementShadeOfColor(index: number, number: number) {
    return this.rgbDeepBlue[number] + index;
  }

  private createLocation(place, index: number) {
    const location = new Location();
    location.city = place.value;
    location.lat = place.y;
    location.lon = place.x;
    this.searchingRoutesForm.at(index).location = location;
  }

  private getIconUrl(index: number): string {
    switch (index) {
      case 0:
        return '../../../assets/images/startMarker.png';
      case this.searchingRoutesForm.length - 1:
        return '../../../assets/images/endMarker.png';
      default:
        return '../../../assets/images/viaMarker.png';
    }
  }

  canAddMoreLocation() {
    return (
      this.searchingRoutesForm.length < this.maxNumberOfLocations &&
      this.currentUser !== null &&
      this.currentUser !== undefined
    );
  }
}
