import { Component, Input, OnDestroy, OnInit } from '@angular/core';
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
  changeOrAddMarker,
  drawPolylineOnMap,
  removeLayer,
  removeMarker,
  removeOneLayer,
} from '../../../util/map-functions';

import { Location } from '../../../model/route/location';
import { VehicleService } from '../../../service/vehicle.service';
import { Vehicle } from '../../../model/vehicle/vehicle';
import { Options } from 'ngx-google-places-autocomplete/objects/options/options';
import { Address } from 'ngx-google-places-autocomplete/objects/address';
import { ToastrService } from 'ngx-toastr';
import { Route } from '../../../model/route/route';
import { DrivingLocation } from '../../../model/route/driving-location';

@Component({
  selector: 'home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements OnInit, OnDestroy {
  routeChoiceView = true;
  filterVehicleView = false;

  @Input() map: L.Map;
  currentUser: User = null;
  isDriver: boolean;
  isRegular: boolean;

  provider1: OpenStreetMapProvider = new OpenStreetMapProvider();

  maxNumberOfLocations: number = 5;
  possibleRoutesViaPoints: PossibleRoutesViaPoints[] = [];
  drawPolylineList: L.Polyline[] = [];
  searchingRoutesForm: SearchingRoutesForm[] = [];
  vehicles: Vehicle[];
  carMarkers: L.Marker[] = [];
  selectedRoute: Route;

  rgbDeepBlue: number[] = [44, 75, 97];
  private authSubscription: Subscription;
  private routeSubscription: Subscription;
  rideIsRequested: boolean;
  responsiveOptions = [
    {
      breakpoint: '1024px',
      numVisible: 1,
      numScroll: 2,
    },
  ];
  options: Options = new Options({
    bounds: undefined,
    fields: ['address_component', 'formatted_address', 'name', 'geometry'],
    strictBounds: false,
    componentRestrictions: { country: 'rs' },
  });

  routePathIndex: number[] = [];

  constructor(
    private routeService: RouteService,
    private authService: AuthService,
    private vehicleService: VehicleService,
    private toast: ToastrService
  ) {
    this.rideIsRequested = false;
  }
  public addressChange(address: Address, index: number) {
    this.deleteMarker(index);
    this.removeAllPolylines();

    this.searchingRoutesForm.at(index).inputPlace = address.formatted_address;

    const lng: number = address.geometry.location.lng();
    const lat: number = address.geometry.location.lat();

    this.addMarker(index, { y: lat, x: lng });

    this.createLocation(address, index);
  }
  ngOnInit(): void {
    this.vehicleService.getAllVehicle().subscribe(vehicleCurrentLocation => {
      this.carMarkers = changeOrAddMarker(
        this.map,
        this.carMarkers,
        this.vehicles,
        vehicleCurrentLocation
      );
    });
    this.searchingRoutesForm.push(new SearchingRoutesForm());
    this.searchingRoutesForm.push(new SearchingRoutesForm());

    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.currentUser = user;
        this.isDriver = this.authService.userIsDriver();
        this.isRegular = this.authService.userIsRegular();
      });

    let div = L.DomUtil.get('route-div');
    L.DomEvent.on(div, 'mousewheel', L.DomEvent.stopPropagation);
    L.DomEvent.on(div, 'click', L.DomEvent.stopPropagation);
  }

  ngOnDestroy(): void {
    for (let i; i < this.searchingRoutesForm.length; i++) {
      this.deleteMarker(i);
    }

    this.carMarkers.forEach(marker => removeMarker(this.map, marker));
    this.removeAllPolylines();
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
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
    if (this.checkIfDeletedLocationIsDestination(index)) {
      this.deleteMarker(index - 1);
      this.addMarkerWithLonAndLat(
        index - 1,
        this.searchingRoutesForm.at(index - 1).location.lon,
        this.searchingRoutesForm.at(index - 1).location.lat
      );
    }
    this.possibleRoutesViaPoints = [];
  }

  getFilteredPlaces(index: number) {
    return this.searchingRoutesForm.at(index).filteredPlaces;
  }

  getPossibleRoutes() {
    this.rideIsRequested = true;

    this.createListDrivingLocation();

    if (!this.formIsInvalid()) {
      const locationsForCreateRoutes: Location[] = [];

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
          this.possibleRoutesViaPoints = res;
          if (res.length > 0) {
            this.changeCurrentRoutes(res);
          } else {
            this.toast.error(
              'Cannot find routes for chosen places',
              'Unavailable routes'
            );
          }
        });
    }
  }

  changeCurrentRoutes(routes: PossibleRoutesViaPoints[]) {
    this.routePathIndex = [];
    this.removeAllPolylines();

    if (routes.length === 1) {
      this.routePathIndex.push(0);

      routes.at(0).possibleRouteDTOList.forEach(oneRoute => {
        const latLongs = this.getLatLongsRoute(oneRoute);
        this.drawPolyline(
          routes.at(0).possibleRouteDTOList.indexOf(oneRoute),
          0,
          latLongs
        );
      });
    } else {
      routes.forEach(route => {
        this.routePathIndex.push(0);
        const latLongs = this.getLatLongsRoute(
          route.possibleRouteDTOList.at(0)
        );
        // drawPolylineOnMapWithoutClickEvent(this.map, latLongs, this.drawPolylineList);
      });
    }
  }

  private getLatLongsRoute(route: PossibleRoute): number[] {
    let latLongs = [];
    route.pointList.forEach(latLng => latLongs.push([latLng[0], latLng[1]]));

    return latLongs;
  }

  private drawPolyline(
    indexOfSelectedPath: number,
    indexOfRouteInPossibleRoutes: number,
    latLongs
  ): void {
    const color: string = indexOfSelectedPath === 0 ? '#283b50' : '#cdd1d3';
    const weight: number = indexOfSelectedPath === 0 ? 9 : 7;
    const polyline: L.Polyline = drawPolylineOnMap(
      this.map,
      latLongs,
      color,
      weight,
      this.drawPolylineList
    );
    const that = this;
    polyline.on('click', function () {
      that.routePathIndex[indexOfRouteInPossibleRoutes] = indexOfSelectedPath;
      that.drawPolylineList.forEach(p => {
        p.setStyle({
          color: '#cdd1d3',
          weight: 7,
        });

        polyline.setStyle({
          color: '#283b50',
          weight: 9,
        });
      });
    });
  }

  changeOptionRouteOnClick(
    route: PossibleRoute,
    indexOfSelectedPath: number,
    indexOfRouteInPossibleRoutes: number
  ): void {
    this.routePathIndex[indexOfRouteInPossibleRoutes] = indexOfSelectedPath;

    if (this.searchingRoutesForm.length === 2) {
      this.drawPolylineList.forEach(p => {
        p.setStyle({
          color: '#cdd1d3',
          weight: 7,
        });
      });
      this.drawPolylineList.at(indexOfSelectedPath).setStyle({
        color: '#283b50',
        weight: 9,
      });
    } else {
      this.removeOnePolyline(indexOfSelectedPath);
      let latLongs = [];
      route.pointList.forEach(latLng => latLongs.push([latLng[0], latLng[1]]));
      let color: string = '#283b50';
      const weight: number = 9;
      drawPolylineOnMap(
        this.map,
        latLongs,
        color,
        weight,
        this.drawPolylineList
      );
    }

    // this.chooseVehicleAndPassengers(route);
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

  private createLocation(address: Address, index: number): void {
    let houseNumber = '';
    let city = '';
    let street = '';
    let zipCode = '';
    for (let i = 0; i < address.address_components.length; i++) {
      const mapAddress = address.address_components[i];
      if (mapAddress.long_name !== '') {
        if (mapAddress.types[0] === 'street_number') {
          houseNumber = mapAddress.long_name;
        }
        if (mapAddress.types[0] === 'route') {
          street = mapAddress.long_name;
        }

        if (mapAddress.types[0] === 'locality') {
          city = mapAddress.long_name;
        }
        if (mapAddress.types[0] === 'administrative_area_level_1') {
          // this.mapState = mapAddress.long_name;
        }
        if (mapAddress.types[0] === 'country') {
          // this.mapcountry = mapAddress.long_name;
        }
        if (mapAddress.types[0] === 'postal_code') {
          zipCode = mapAddress.long_name;
        }
      }
    }
    const lng: number = address.geometry.location.lng();
    const lat: number = address.geometry.location.lat();
    this.searchingRoutesForm.at(index).location = {
      city: city,
      lon: lng,
      lat: lat,
      street: street,
      number: houseNumber,
      zipCode: zipCode,
    };
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

  chooseVehicleAndPassengers(): void {
    console.log(this.createRoute());
    this.selectedRoute = this.createRoute();
    this.routeChoiceView = false;
    this.filterVehicleView = true;
  }

  private checkIfDeletedLocationIsDestination(index: number) {
    return this.searchingRoutesForm.length === index;
  }

  formIsInvalid(): boolean {
    let formIsInvalid: boolean = false;
    this.searchingRoutesForm.forEach(searchingRoute => {
      if (
        searchingRoute.location === undefined ||
        searchingRoute.location === null
      ) {
        formIsInvalid = true;
      }
    });

    return formIsInvalid;
  }

  fieldIsInvalid(index: number): boolean {
    return (
      this.searchingRoutesForm.at(index).location === undefined ||
      this.searchingRoutesForm.at(index).location === null
    );
  }

  getFromToLabel(index: number): string {
    const location: Location = this.searchingRoutesForm.at(index).location;

    return `${location.street} ${location.number}`;
  }

  private createListDrivingLocation(): DrivingLocation[] {
    const drivingLocations: DrivingLocation[] = [];
    this.searchingRoutesForm.forEach(searchingRoute => {
      drivingLocations.push({
        index: this.searchingRoutesForm.indexOf(searchingRoute) + 1,
        location: searchingRoute.location,
      });
    });

    return drivingLocations;
  }

  private createRoute(): Route {
    return {
      locations: this.createListDrivingLocation(),
      distance: this.calculateDistance(),
      timeInMin: this.calculateMinutes(),
    };
  }

  private calculateMinutes(): number {
    let minutes = 0;
    this.routePathIndex.forEach(index => {
      minutes += this.possibleRoutesViaPoints
        .at(this.routePathIndex.indexOf(index))
        .possibleRouteDTOList.at(index).timeInMin;
    });

    return minutes;
  }

  private calculateDistance(): number {
    let distance = 0;
    this.routePathIndex.forEach(index => {
      distance += this.possibleRoutesViaPoints
        .at(this.routePathIndex.indexOf(index))
        .possibleRouteDTOList.at(index).distance;
    });

    return distance;
  }
}
