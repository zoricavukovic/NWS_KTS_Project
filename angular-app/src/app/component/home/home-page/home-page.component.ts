import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {SearchingRoutesForm} from '../../../model/route/searching-routes-form';
import {RouteService} from '../../../service/route.service';
import {PossibleRoute} from '../../../model/route/possible-routes';
import {User} from '../../../model/user/user';
import {Subscription} from 'rxjs';
import {AuthService} from '../../../service/auth.service';
import {PossibleRoutesViaPoints} from '../../../model/route/possible-routes-via-points';
import {Location} from '../../../model/route/location';
import {VehicleService} from '../../../service/vehicle.service';
import {Vehicle} from '../../../model/vehicle/vehicle';
import {Options} from "ngx-google-places-autocomplete/objects/options/options";
import {Address} from "ngx-google-places-autocomplete/objects/address";
import {ToastrService} from "ngx-toastr";
import {Route} from "../../../model/route/route";
import {DrivingLocation} from "../../../model/route/driving-location";
import {
  addCarMarkers,
  addMarker,
  drawPolylineOnMap,
  getRouteCoordinates,
  removeAllPolyline,
  removeLine,
  removeMarker
} from "../../../util/map-functions";

@Component({
  selector: 'home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements OnInit, OnDestroy {
  routeChoiceView = true;
  filterVehicleView = false;

  @Input() map: google.maps.Map;
  currentUser: User = null;
  isDriver: boolean;
  isRegular: boolean;

  maxNumberOfLocations = 5;
  possibleRoutesViaPoints: PossibleRoutesViaPoints[] = [];
  drawPolylineList: google.maps.Polyline[] = [];
  searchingRoutesForm: SearchingRoutesForm[] = [];
  vehicles: Vehicle[];
  carMarkers: google.maps.Marker[] = [];
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
    bounds: undefined, fields: ["address_component", "formatted_address","name", "geometry"], strictBounds: false,
    componentRestrictions: {country: 'rs'}
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

    this.searchingRoutesForm.at(index).marker = addMarker(this.map, {lat: lat, lng: lng});
    this.searchingRoutesForm.at(index).marker.setIcon(this.getIconUrl(index));
    this.createLocation(address, index);

  }

  ngOnInit(): void {
    this.vehicleService.getAllVehicle().subscribe(vehicleCurrentLocation => {
      this.carMarkers = addCarMarkers(
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
      }
    );

  }

  ngOnDestroy(): void {
    for (let i; i < this.searchingRoutesForm.length; i++) {
      this.deleteMarker(i);
    }

    this.carMarkers.forEach(marker => removeMarker(marker));
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
    this.removeAllPolylines();

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
          }
          else {
            this.toast.error('Cannot find routes for chosen places', 'Unavailable routes');
          }
        });
    }
  }

  changeCurrentRoutes(routes: PossibleRoutesViaPoints[]) {
    this.routePathIndex = [];
    this.removeAllPolylines();

    if (routes.length === 1){
      this.routePathIndex.push(0);

      routes.at(0).possibleRouteDTOList.forEach(oneRoute => {
        const routeCoordinates = getRouteCoordinates(oneRoute);
        this.drawPolyline(routes.at(0).possibleRouteDTOList.indexOf(oneRoute), 0, routeCoordinates);
      })
    }
    else{
      routes.forEach(route => {
        this.routePathIndex.push(0);
        const routeCoordinates = getRouteCoordinates(route.possibleRouteDTOList.at(0));
        this.drawPolylineList.push(drawPolylineOnMap(this.map, routeCoordinates,"#283b50" , 9));

      });
    }
  }


  private drawPolyline(indexOfSelectedPath: number, indexOfRouteInPossibleRoutes: number, latLongs: google.maps.LatLngLiteral[]): void {
    const color: string = indexOfSelectedPath === 0 ? "#283b50" : "#cdd1d3";
    const weight: number = indexOfSelectedPath === 0 ? 9 : 7;
    const polyline: google.maps.Polyline = drawPolylineOnMap(this.map, latLongs, color, weight);
    polyline.setOptions({clickable: true});
    this.drawPolylineList[indexOfSelectedPath] = polyline;
    const that = this;
    google.maps.event.addListener(polyline, 'click', function() {
      that.routePathIndex[indexOfRouteInPossibleRoutes] = indexOfSelectedPath;
      that.drawPolylineList.forEach(p => {
        p.setOptions({
          strokeColor: '#cdd1d3',
          strokeWeight: 7,
        });

        polyline.setOptions({
          strokeColor: '#283b50',
          strokeWeight: 9,
        });
      });
    });

  }

  changeOptionRouteOnClick(route: PossibleRoute, indexOfSelectedPath: number, indexOfRouteInPossibleRoutes: number): void {
    this.routePathIndex[indexOfRouteInPossibleRoutes] = indexOfSelectedPath;

    if (this.hasOneDestination()){
      this.swapColorsOfRoutes(indexOfSelectedPath);
    } else {
      this.removeOnePolyline(indexOfRouteInPossibleRoutes);
      const routeCoordinates = getRouteCoordinates(route);
      this.drawPolylineList[indexOfRouteInPossibleRoutes] = drawPolylineOnMap(this.map, routeCoordinates, "#283b50", 9);
    }
  }

  private hasOneDestination() {

    return this.searchingRoutesForm.length === 2;
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
    for(let i=0; i<address.address_components.length;i++)
    {
      const mapAddress = address.address_components[i];
      if(mapAddress.long_name !==''){

        if(mapAddress.types[0] === "street_number"){
          houseNumber = mapAddress.long_name;
        }
        if(mapAddress.types[0] === "route"){
          street = mapAddress.long_name;
        }

        if(mapAddress.types[0] === "locality"){
          city  = mapAddress.long_name;
        }
        if(mapAddress.types[0] === "administrative_area_level_1"){
          // this.mapState = mapAddress.long_name;
        }
        if(mapAddress.types[0] === "country"){
          // this.mapcountry = mapAddress.long_name;
        }
        if(mapAddress.types[0] === "postal_code"){
          zipCode  = mapAddress.long_name;
        }
      }

    }
    const lng: number = address.geometry.location.lng();
    const lat: number = address.geometry.location.lat();
    this.searchingRoutesForm.at(index).location = {
      "city": city,
      "lon": lng,
      "lat": lat,
      "street": street,
      "number": houseNumber,
      "zipCode": zipCode
    }
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


  private addMarkerWithLonAndLat(index: number, lon: number, lat: number) {
    const markerCoordinates: google.maps.LatLngLiteral = {lat: lat, lng: lon};
    this.searchingRoutesForm.at(index).marker = addMarker(this.map, markerCoordinates);
    this.searchingRoutesForm.at(index).marker.setIcon(this.getIconUrl(index));
  }

  private deleteMarker(index: number) {
    if (
      this.searchingRoutesForm.at(index).marker !== null &&
      this.searchingRoutesForm.at(index).marker !== undefined
    ) {
      removeMarker(this.searchingRoutesForm.at(index).marker);
      this.removeAllPolylines();
    }
  }

  private removeAllPolylines() {
    if (this.polylineFound()) {
      removeAllPolyline(this.drawPolylineList);
      this.drawPolylineList = [];
    }
  }

  private removeOnePolyline(index: number) {
    if (this.drawPolylineList.at(index)) {
      removeLine(this.drawPolylineList.at(index));
      this.drawPolylineList[index] = null;
    }
  }

  chooseVehicleAndPassengers(): void {
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
      if (searchingRoute.location === undefined || searchingRoute.location === null){
        formIsInvalid = true;
      }
    })

    return formIsInvalid;
  }

  fieldIsInvalid(index: number): boolean {

    return this.searchingRoutesForm.at(index).location === undefined ||
      this.searchingRoutesForm.at(index).location === null;
  }

  getFromToLabel(index: number): string {
    const location: Location = this.searchingRoutesForm.at(index).location;

    return `${location.street} ${location.number}`;
  }

  private createListDrivingLocation(): DrivingLocation[] {
    const drivingLocations: DrivingLocation[] = [];
    this.searchingRoutesForm.forEach(searchingRoute => {
      drivingLocations.push({
        index:  this.searchingRoutesForm.indexOf(searchingRoute) + 1,
        location: searchingRoute.location
      });
    })

    return drivingLocations;
  }

  private createRoute(): Route {

    return {
      locations: this.createListDrivingLocation(),
      distance: this.calculateDistance(),
      timeInMin: this.calculateMinutes()
    }

  }

  private calculateMinutes(): number {
    let minutes = 0;
    this.routePathIndex.forEach(index => {
      minutes += this.possibleRoutesViaPoints.at(this.routePathIndex.indexOf(index)).possibleRouteDTOList.at(index).timeInMin;
    });

    return minutes;
  }

  private calculateDistance(): number {
    let distance = 0;
    this.routePathIndex.forEach(index => {
      distance += this.possibleRoutesViaPoints.at(this.routePathIndex.indexOf(index)).possibleRouteDTOList.at(index).distance;
    });

    return distance;
  }

  public chooseFastestRoute() {
    if (this.hasOneDestination()){
      const minTimePath = this.possibleRoutesViaPoints.at(0).possibleRouteDTOList
        .reduce((a,b)=>
          a.timeInMin < b.timeInMin ? a:b
        );
      const indexOfSelectedPath =  this.possibleRoutesViaPoints.at(0).possibleRouteDTOList.indexOf(minTimePath);
      this.routePathIndex[0] = indexOfSelectedPath;
      this.swapColorsOfRoutes(indexOfSelectedPath);

    }
    else{
      this.removeAllPolylines();
      this.possibleRoutesViaPoints.forEach(route => {
        const minTimePath = route.possibleRouteDTOList.reduce((a,b)=>
          a.timeInMin < b.timeInMin ? a:b
        );
        const indexOfSelectedPath = route.possibleRouteDTOList.indexOf(minTimePath);
        this.routePathIndex[this.possibleRoutesViaPoints.indexOf(route)] = indexOfSelectedPath;
        const routeCoordinates = getRouteCoordinates(route.possibleRouteDTOList.at(indexOfSelectedPath));
        this.drawPolylineList.push(drawPolylineOnMap(this.map, routeCoordinates, "#283b50", 9));
      })
    }

  }

  public chooseShortestRoute() {
    if (this.hasOneDestination()){
      const minTimePath = this.possibleRoutesViaPoints.at(0).possibleRouteDTOList
        .reduce((a,b)=>
          a.distance < b.distance ? a:b
        );
      const indexOfSelectedPath =  this.possibleRoutesViaPoints.at(0).possibleRouteDTOList.indexOf(minTimePath);
      this.routePathIndex[0] = indexOfSelectedPath;
      this.swapColorsOfRoutes(indexOfSelectedPath);

    }
    else{
      this.removeAllPolylines();
      this.possibleRoutesViaPoints.forEach(route => {
        const minDistancePath = route.possibleRouteDTOList.reduce((a,b)=>
          a.distance < b.distance ? a:b
        );
        const indexOfSelectedPath = route.possibleRouteDTOList.indexOf(minDistancePath);
        this.routePathIndex[this.possibleRoutesViaPoints.indexOf(route)] = indexOfSelectedPath;
        const routeCoordinates = getRouteCoordinates(route.possibleRouteDTOList.at(indexOfSelectedPath));
        this.drawPolylineList.push(drawPolylineOnMap(this.map, routeCoordinates, "#283b50", 9));
      })
    }
  }


  private swapColorsOfRoutes(indexOfSelectedPath: number): void {
    this.drawPolylineList.forEach(p => {
      p.setOptions({
        strokeColor: '#cdd1d3',
        strokeWeight: 7
      });
    });
    this.drawPolylineList.at(indexOfSelectedPath).setOptions({
      strokeColor: '#283b50',
      strokeWeight: 9
    });
  }
}
