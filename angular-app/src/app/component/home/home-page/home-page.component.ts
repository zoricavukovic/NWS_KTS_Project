import {AfterViewInit, Component, Input, OnDestroy, OnInit} from '@angular/core';
import * as L from 'leaflet';
import { OpenStreetMapProvider } from 'leaflet-geosearch';
import { SearchingRoutesForm } from '../../../model/route/searching-routes-form';
import { RouteService } from '../../../service/route.service';
import { PossibleRoute } from '../../../model/route/possible-routes';
import { User } from '../../../model/user/user';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../service/auth.service';
import { PossibleRoutesViaPoints } from '../../../model/route/possible-routes-via-points';
import {
  addCarMarker, changeOrAddMarker,
  drawPolylineOnMap, refreshMap,
  removeLayer,
  removeMarker,
  removeOneLayer,
} from '../../../util/map-functions';
import { Location } from '../../../model/route/location';
import {VehicleService} from "../../../service/vehicle.service";
import {Vehicle} from "../../../model/vehicle/vehicle";

@Component({
  selector: 'home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements OnInit, OnDestroy {
  routeChoiceView = true;
  filterVehicleView = false;

  @Input() map;
  currentUser: User;
  provider1: OpenStreetMapProvider = new OpenStreetMapProvider();

  maxNumberOfLocations = 5;
  possibleRoutesViaPoints: PossibleRoutesViaPoints[] = [];
  drawPolylineList = [];
  searchingRoutesForm: SearchingRoutesForm[] = [];
  currentUserIsDriver: boolean;
  vehicles: Vehicle[];
  carMarkers: L.Marker[] = []
  /* autocompleteForm = new FormGroup({
    startDest: new FormControl(undefined, [this.requireMatch.bind(this)]),
    endDest: new FormControl(undefined, [this.requireMatch.bind(this)]),
  });*/

  rgbDeepBlue: number[] = [44, 75, 97];
  private authSubscription: Subscription;
  private routeSubscription: Subscription;

  a: string[];

  constructor(
    private routeService: RouteService,
    private authService: AuthService,
    private vehicleService: VehicleService
  ) {
  }

  ngOnInit(): void {
    this.vehicleService.getAllVehicle().subscribe(vehicleCurrentLocation => {
      this.carMarkers = changeOrAddMarker(this.map, this.carMarkers, this.vehicles, vehicleCurrentLocation)
      }
    )
    this.searchingRoutesForm.push(new SearchingRoutesForm());
    this.searchingRoutesForm.push(new SearchingRoutesForm());
    this.currentUser = this.authService.getCurrentUser;
    this.currentUserIsDriver = this.currentUser?.userIsDriver();
    // let div = L.DomUtil.get('route-div');
    // L.DomEvent.on(div, 'mousewheel', L.DomEvent.stopPropagation);
    // L.DomEvent.on(div, 'click', L.DomEvent.stopPropagation);
  }

  ngOnDestroy(): void {
    for (let i; i < this.searchingRoutesForm.length; i++){
      this.deleteMarker(i);
    }

    this.carMarkers.forEach(marker => removeMarker(this.map, marker))
    this.removeAllPolylines();
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }


  async initMap() {
    this.map = L.map('map').setView([45.25167, 19.83694], 13);

    L.tileLayer('//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      crossOrigin: true,
    }).addTo(this.map);
    L.control.zoom({
      position: 'topright'
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
    if (this.checkIfDeletedLocationIsDestination(index)){
      this.deleteMarker(index-1);
      this.addMarkerWithLonAndLat(index - 1, this.searchingRoutesForm.at(index-1).location.lon,
        this.searchingRoutesForm.at(index-1).location.lat
      );
    }
    this.possibleRoutesViaPoints = [];
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
        this.routeService.createLocationForRoutesRequest(
          locationsForCreateRoutes
        )
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
    const location: Location = {
      city: place.value,
      lat: place.y,
      lon: place.x,
    };
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

  currentUserIsRegular() {

    return this.authService.getCurrentUser?.userIsRegular();
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

  private addMarkerWithLonAndLat(index: number, lat: number, lon: number) {
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
      [lon, lat],
      markerOptions
    );
    this.searchingRoutesForm.at(index).marker.addTo(this.map);
    this.map.panBy(L.point(lon, lat));
  }

  private deleteMarker(index: number) {
    if (this.searchingRoutesForm.at(index).marker !== null && this.searchingRoutesForm.at(index).marker !== undefined) {
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

  private checkIfDeletedLocationIsDestination(index: number) {
    return this.searchingRoutesForm.length === index;
  }
}
