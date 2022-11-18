import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import * as L from 'leaflet';
import { OpenStreetMapProvider } from 'leaflet-geosearch';
import { SearchingRoutesForm } from '../../../model/searching-routes-form';
import { Location } from '../../../model/response/location';
import { RouteService } from '../../../service/route.service';
import { LocationsForRoutesRequest } from '../../../model/request/locations-for-routes-request';
import { PossibleRoute } from '../../../model/response/possible-routes';
import { User } from '../../../model/response/user/user';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../service/auth.service';
import { PossibleRoutesViaPoints } from '../../../model/response/possible-routes-via-points';
import {
  drawPolylineOnMap,
  removeLayer,
  removeMarker,
  removeOneLayer,
} from '../../../util/map-functions';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements OnInit, OnDestroy, AfterViewInit {
  routeChoiceView = true;
  filterVehicleView = false;

  private map: L.Map;
  currentUser: User;
  provider1: OpenStreetMapProvider = new OpenStreetMapProvider();

  maxNumberOfLocations = 5;
  possibleRoutesViaPoints: PossibleRoutesViaPoints[] = [];
  drawPolylineList = [];
  searchingRoutesForm: SearchingRoutesForm[] = [];
  currentUserIsDriver: boolean;
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
    this.currentUser = this.authService.getCurrentUser;
    this.currentUserIsDriver = this.currentUser?.userIsDriver();
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
    let div = L.DomUtil.get('route-div');
    L.DomEvent.on(div, 'mousewheel', L.DomEvent.stopPropagation);
    L.DomEvent.on(div, 'click', L.DomEvent.stopPropagation);
  }

  addOneMoreLocation() {
    this.possibleRoutesViaPoints = [];
    this.searchingRoutesForm.splice(
      this.searchingRoutesForm.length - 1,
      0,
      new SearchingRoutesForm()
    );
  }

  deleteOneLocation(index: number) {
    this.deleteMarker(index);
    this.removeAllPolylines();
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
    this.deleteMarker(index);
    this.removeAllPolylines();

    this.addMarker(index, place);

    this.createLocation(place, index);
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
    console.log(this.searchingRoutesForm);
    this.searchingRoutesForm.forEach(searchingRoutesLocation =>
      locationsForCreateRoutes.push(searchingRoutesLocation.location)
    );

    this.routeSubscription = this.routeService
      .getPossibleRoutes(
        new LocationsForRoutesRequest(locationsForCreateRoutes)
      )
      .subscribe(res => {
        console.log(res);
        this.possibleRoutesViaPoints = res;
        if (res.length > 0) {
          this.changeCurrentRoutes(res);
        }
      });
  }

  changeCurrentRoutes(routes: PossibleRoutesViaPoints[]) {
    this.removeAllPolylines();

    let index: number = 0;
    routes.forEach(route => {
      let latLongs = this.getLatLongsForFirstRoute(route);
      this.drawPolyline(index, latLongs);
      index++;
    });
  }

  private getLatLongsForFirstRoute(route: PossibleRoutesViaPoints) {
    let latLongs = [];
    route.possibleRouteDTOList
      .at(0)
      .pointList.forEach(latLng => latLongs.push([latLng[0], latLng[1]]));

    return latLongs;
  }

  private drawPolyline(index: number, latLongs) {
    let color: string = this.getPolylineColor(index);
    drawPolylineOnMap(this.map, latLongs, color, this.drawPolylineList);
  }

  changeOptionRouteOnClick(route: PossibleRoute, idx: number) {
    this.removeOnePolyline(idx);

    let latLongs = [];
    route.pointList.forEach(latLng => latLongs.push([latLng[0], latLng[1]]));
    let color: string = this.getPolylineColor(idx);
    drawPolylineOnMap(this.map, latLongs, color, this.drawPolylineList);
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

  private polylineFound() {
    return (
      this.drawPolylineList !== null && this.drawPolylineList !== undefined
    );
  }

  private incrementShadeOfColor(index: number, number: number) {
    return this.rgbDeepBlue[number] + index;
  }

  private createLocation(place, index: number) {
    let loc = new Location();
    loc.city = place.value;
    loc.lat = place.y;
    loc.lon = place.x;
    this.searchingRoutesForm.at(index).location = loc;
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

  private addMarker(index: number, place) {
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
    this.map.panBy(L.point(place.y, place.x));
  }

  private deleteMarker(index: number) {
    if (
      this.searchingRoutesForm.at(index).marker !== null &&
      this.searchingRoutesForm.at(index).marker !== undefined
    ) {
      removeMarker(this.map, this.searchingRoutesForm.at(index).marker);
    }
  }

  private removeAllPolylines() {
    if (this.polylineFound()) {
      removeLayer(this.map, this.drawPolylineList);
    }
  }

  private removeOnePolyline(index: number) {
    if (this.drawPolylineList.at(index)) {
      removeOneLayer(this.map, this.drawPolylineList.at(index));
    }
  }

  private getPolylineColor(index: number): string {
    return `rgb(${this.incrementShadeOfColor(index * 5, 0)},
     ${this.incrementShadeOfColor(index * 5, 1)}, ${this.incrementShadeOfColor(
      index * 5,
      2
    )})`;
  }
}
