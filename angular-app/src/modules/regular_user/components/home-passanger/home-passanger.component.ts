import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Location} from "../../../shared/models/route/location";
import {Subscription} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {Address} from "ngx-google-places-autocomplete/objects/address";
import {Options} from "ngx-google-places-autocomplete/objects/options/options";
import SockJS from "sockjs-client";
import {Stomp} from "@stomp/stompjs";
import {environment} from "../../../../environments/environment";
import {Router} from "@angular/router";
import { FormArray, FormBuilder, FormControl, FormGroup } from '@angular/forms';
import {PossibleRoute} from "../../../shared/models/route/possible-routes";
import {User} from "../../../shared/models/user/user";
import {DrivingLocation} from "../../../shared/models/route/driving-location";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {DrivingService} from "../../../shared/services/driving-service/driving.service";
import {SimpleDrivingInfo} from "../../../shared/models/driving/simple-driving-info";
import {RouteService} from "../../../shared/services/route-service/route.service";
import {VehicleService} from "../../../shared/services/vehicle-service/vehicle.service";
import {PossibleRoutesViaPoints} from "../../../shared/models/route/possible-routes-via-points";
import {SearchingRoutesForm} from "../../../shared/models/route/searching-routes-form";
import {Route} from "../../../shared/models/route/route";
import {
  addMarker,
  drawPolylineOnMap,
  getRouteCoordinates, removeAllMarkers,
  removeAllPolyline, removeLine,
  removeMarker
} from "../../../shared/utils/map-functions";

@Component({
  selector: 'app-home-passanger',
  templateUrl: './home-passanger.component.html',
  styleUrls: ['./home-passanger.component.css']
})
export class HomePassangerComponent implements OnInit, OnDestroy {
  @Input() map: google.maps.Map;
  @Input() currentUser: User;

  routeChoiceView: boolean;
  filterVehicleView: boolean;
  rideRequestForm: FormGroup;

  get searchingForm(){
    return this.rideRequestForm.get('searchingRoutesForm')['controls'];
  }

  createEmptySearchForm(): FormGroup{
    return this.formBuilder.group(new SearchingRoutesForm());
  }

  createEmptyRoute(): Route{
    return {
      locations: [],
      distance: 0,
      timeInMin: 0,
      routePathIndex: []
    }
  }

  possibleRoutesViaPoints: PossibleRoutesViaPoints[] = [];
  drawPolylineList: google.maps.Polyline[] = [];
  selectedRoute: Route;
  routePathIndex: number[] = [];
  loadingViewVar = false;

  responsiveOptions = [
    {
      breakpoint: '1024px',
      numVisible: 1,
      numScroll: 2,
    },
  ];

  authSubscription: Subscription;
  routeSubscription: Subscription;
  drivingSubscription: Subscription;
  maxNumberOfLocations = 5;
  rideIsRequested: boolean;
  activeRide: SimpleDrivingInfo;

  options: Options = new Options({
    bounds: undefined,
    fields: ['address_component', 'formatted_address', 'name', 'geometry'],
    strictBounds: false,
    componentRestrictions: { country: 'rs' },
  });
  private stompClient: any;


  constructor(
    private routeService: RouteService,
    private authService: AuthService,
    private vehicleService: VehicleService,
    private drivingService: DrivingService,
    private toast: ToastrService,
    private router: Router,
    private formBuilder: FormBuilder
  ) {
    this.routeChoiceView = true;
    this.filterVehicleView = false;
    this.rideIsRequested = false;
    this.activeRide = null;
    this.rideRequestForm = new FormGroup({
      searchingRoutesForm: this.formBuilder.array([this.createEmptySearchForm(), this.createEmptySearchForm()]),
      selectedRoute: new FormControl(this.createEmptyRoute()),
      routePathIndex: new FormControl([]),
      petFriendly: new FormControl(false),
      babySeat: new FormControl(false),
      vehicleType: new FormControl(''),
      price: new FormControl(0),
      senderEmail: new FormControl(''),
      selectedPassengers: new FormControl([])
    })
  }

  ngOnInit(): void {
    this.initializeWebSocketConnection();
    this.drivingSubscription = this.drivingService.checkIfUserHasActiveDriving(this.currentUser.id).subscribe(
      res => {
        this.activeRide = res;
  
      }
    )
  }

  getPossibleRoutes() {

    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
    if (!this.formIsInvalid()) {
      const locationsForCreateRoutes: Location[] = [];

      this.searchingForm.forEach(searchingRoutesLocation =>
        locationsForCreateRoutes.push(searchingRoutesLocation.value.location)
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
    this.rideRequestForm.get('routePathIndex').setValue([]);
    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);

    if (routes.length === 1) {
      this.rideRequestForm.get('routePathIndex')?.value.push(0);

      routes.at(0).possibleRouteDTOList.forEach(oneRoute => {
        const routeCoordinates = getRouteCoordinates(oneRoute);
        this.drawPolyline(
          routes.at(0).possibleRouteDTOList.indexOf(oneRoute),
          0,
          routeCoordinates
        );
      });
    } else {
      routes.forEach(route => {
        this.rideRequestForm.get('routePathIndex')?.value.push(0);
        const routeCoordinates = getRouteCoordinates(
          route.possibleRouteDTOList.at(0)
        );
        this.drawPolylineList.push(
          drawPolylineOnMap(this.map, routeCoordinates, '#283b50', 9)
        );
      });
    }
  }

  private drawPolyline(
    indexOfSelectedPath: number,
    indexOfRouteInPossibleRoutes: number,
    latLongs: google.maps.LatLngLiteral[]
  ): void {
    const color: string = indexOfSelectedPath === 0 ? '#283b50' : '#cdd1d3';
    const weight: number = indexOfSelectedPath === 0 ? 9 : 7;
    const polyline: google.maps.Polyline = drawPolylineOnMap(
      this.map,
      latLongs,
      color,
      weight
    );
    polyline.setOptions({ clickable: true });
    this.drawPolylineList[indexOfSelectedPath] = polyline;
    const that = this;
    google.maps.event.addListener(polyline, 'click', function () {
      (that.rideRequestForm.get('routePathIndex')?.value)[indexOfRouteInPossibleRoutes] = indexOfSelectedPath;
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

  getFromToLabel(index: number): string {
    const location: Location = this.searchingForm.at(index).value.location;

    return `${location.street} ${location.number}`;
  }

  changeOptionRouteOnClick(
    route: PossibleRoute,
    indexOfSelectedPath: number,
    indexOfRouteInPossibleRoutes: number
  ): void {
    (this.rideRequestForm.get('routePathIndex')?.value)[indexOfRouteInPossibleRoutes] = indexOfSelectedPath;

    if (this.hasOneDestination()) {
      this.swapColorsOfRoutes(indexOfSelectedPath);
    } else {
      this.removeOnePolyline(indexOfRouteInPossibleRoutes);
      const routeCoordinates = getRouteCoordinates(route);
      this.drawPolylineList[indexOfRouteInPossibleRoutes] = drawPolylineOnMap(
        this.map,
        routeCoordinates,
        '#283b50',
        9
      );
    }
  }

  private createListDrivingLocation(): DrivingLocation[] {
    const drivingLocations: DrivingLocation[] = [];
    this.searchingForm.forEach(searchingRoute => {
      drivingLocations.push({
        index: this.searchingForm.indexOf(searchingRoute) + 1,
        location: searchingRoute.value.location,
      });
    });

    return drivingLocations;
  }

  private createRoute(): Route {
    return {
      locations: this.createListDrivingLocation(),
      distance: this.calculateDistance(),
      timeInMin: this.calculateMinutes(),
      routePathIndex: this.rideRequestForm.get('routePathIndex').value
    };
  }

  private calculateMinutes(): number {
    let minutes = 0;
    this.rideRequestForm.get('routePathIndex').value.forEach(index => {
      minutes += this.possibleRoutesViaPoints
        .at(this.rideRequestForm.get('routePathIndex').value.indexOf(index))
        .possibleRouteDTOList.at(index).timeInMin;
    });

    return minutes;
  }

  private calculateDistance(): number {
    let distance = 0;
    this.rideRequestForm.get('routePathIndex').value.forEach(index => {
      distance += this.possibleRoutesViaPoints
        .at(this.rideRequestForm.get('routePathIndex').value.indexOf(index))
        .possibleRouteDTOList.at(index).distance;
    });

    return distance;
  }

  private swapColorsOfRoutes(indexOfSelectedPath: number): void {
    this.drawPolylineList.forEach(p => {
      p.setOptions({
        strokeColor: '#cdd1d3',
        strokeWeight: 7,
      });
    });
    this.drawPolylineList.at(indexOfSelectedPath).setOptions({
      strokeColor: '#283b50',
      strokeWeight: 9,
    });
  }

  private hasOneDestination(): boolean {

    return this.searchingForm.length === 2;
  }

  private removeOnePolyline(index: number) {
    if (this.drawPolylineList.at(index)) {
      removeLine(this.drawPolylineList.at(index));
      this.drawPolylineList[index] = null;
    }
  }

  public chooseFastestRoute() {
    if (this.hasOneDestination()) {
      const minTimePath = this.possibleRoutesViaPoints
        .at(0)
        .possibleRouteDTOList.reduce((a, b) =>
          a.timeInMin < b.timeInMin ? a : b
        );
      const indexOfSelectedPath = this.possibleRoutesViaPoints
        .at(0)
        .possibleRouteDTOList.indexOf(minTimePath);
      (this.rideRequestForm.get('routePathIndex')?.value)[0] = indexOfSelectedPath;
      this.swapColorsOfRoutes(indexOfSelectedPath);
    } else {
      this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
      this.possibleRoutesViaPoints.forEach(route => {
        const minTimePath = route.possibleRouteDTOList.reduce((a, b) =>
          a.timeInMin < b.timeInMin ? a : b
        );
        const indexOfSelectedPath =
          route.possibleRouteDTOList.indexOf(minTimePath);
          this.searchingForm[this.possibleRoutesViaPoints.indexOf(route)] =
          indexOfSelectedPath;
        const routeCoordinates = getRouteCoordinates(
          route.possibleRouteDTOList.at(indexOfSelectedPath)
        );
        this.drawPolylineList.push(
          drawPolylineOnMap(this.map, routeCoordinates, '#283b50', 9)
        );
      });
    }
  }

  public chooseShortestRoute() {
    if (this.hasOneDestination()) {
      const minTimePath = this.possibleRoutesViaPoints
        .at(0)
        .possibleRouteDTOList.reduce((a, b) =>
          a.distance < b.distance ? a : b
        );
      const indexOfSelectedPath = this.possibleRoutesViaPoints
        .at(0)
        .possibleRouteDTOList.indexOf(minTimePath);
        (this.rideRequestForm.get('routePathIndex')?.value)[0] = indexOfSelectedPath;
      this.swapColorsOfRoutes(indexOfSelectedPath);
    } else {
      this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
      this.possibleRoutesViaPoints.forEach(route => {
        const minDistancePath = route.possibleRouteDTOList.reduce((a, b) =>
          a.distance < b.distance ? a : b
        );
        const indexOfSelectedPath =
          route.possibleRouteDTOList.indexOf(minDistancePath);
          (this.rideRequestForm.get('routePathIndex')?.value)[this.possibleRoutesViaPoints.indexOf(route)] =
          indexOfSelectedPath;
        const routeCoordinates = getRouteCoordinates(
          route.possibleRouteDTOList.at(indexOfSelectedPath)
        );
        this.drawPolylineList.push(
          drawPolylineOnMap(this.map, routeCoordinates, '#283b50', 9)
        );
      });
    }
  }

  chooseVehicleAndPassengers(): void {
    this.rideRequestForm.get('selectedRoute')?.setValue(this.createRoute());
    this.routeChoiceView = false;
    this.filterVehicleView = true;
  }

  formIsInvalid(): boolean {
    let formIsInvalid = false;
    (this.searchingForm).forEach(searchingRoute => {
      if (
        searchingRoute.value.location === undefined ||
        searchingRoute.value.location === null
      ) {
        formIsInvalid = true;
      }
    });

    return formIsInvalid;
  }





  ngOnDestroy(): void {
    removeAllMarkers(this.searchingForm);

    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
    if (this.drivingSubscription) {
      this.drivingSubscription.unsubscribe();
    }
  }


  public addressChange(address: Address, index: number) {
    this.deleteMarker(index);
    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);

    (this.searchingForm).at(index).inputPlace = address.formatted_address;
    const lng: number = address.geometry.location.lng();
    const lat: number = address.geometry.location.lat();

    this.rideRequestForm.get('searchingRoutesForm').value.at(index).marker = addMarker(this.map, {
      lat: lat,
      lng: lng,
    });
    this.rideRequestForm.get('searchingRoutesForm').value.at(index).marker.setIcon(this.getIconUrl(index));
    this.createLocation(address, index);
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
        if (mapAddress.types[0] === 'postal_code') {
          zipCode = mapAddress.long_name;
        }
      }
    }
    const lng: number = address.geometry.location.lng();
    const lat: number = address.geometry.location.lat();
    this.rideRequestForm.get('searchingRoutesForm').value.at(index).location = {
      city: city,
      lon: lng,
      lat: lat,
      street: street,
      number: houseNumber,
      zipCode: zipCode,
    };
  }

  canAddMoreLocation() {
    return (
      this.searchingForm.length < this.maxNumberOfLocations &&
      this.currentUser !== null &&
      this.currentUser !== undefined
    );
  }

  addOneMoreLocation() {
    this.possibleRoutesViaPoints = [];

    (this.rideRequestForm.get('searchingRoutesForm') as FormArray).insert(this.searchingForm.length - 1,
    this.createEmptySearchForm())

  }

  deleteOneLocation(index: number) {
    this.deleteMarker(index);
    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
    (this.rideRequestForm.get('searchingRoutesForm') as FormArray).removeAt(index);
    if (this.checkIfDeletedLocationIsDestination(index)) {
      this.deleteMarker(index - 1);
      this.addMarkerWithLonAndLat(
        index - 1,
        this.searchingForm.at(index - 1).location.lon,
        this.searchingForm.at(index - 1).location.lat
      );
    }
    this.possibleRoutesViaPoints = [];
  }

  hasActiveDriving():boolean {

    return this.activeRide !== null && this.activeRide !== undefined;
  }

  private addMarkerWithLonAndLat(index: number, lon: number, lat: number) {
    const markerCoordinates: google.maps.LatLngLiteral = { lat: lat, lng: lon };
    (this.searchingForm).at(index).marker = addMarker(
      this.map,
      markerCoordinates
    );
    (this.searchingForm).at(index).marker.setIcon(this.getIconUrl(index));
  }

  fieldIsInvalid(index: number): boolean {
    return (
      (this.searchingForm).at(index).location === undefined ||
      (this.searchingForm).at(index).location === null
    );
  }

  private getIconUrl(index: number): string {
    switch (index) {
      case 0:
        return '../../../assets/images/startMarker.png';
      case this.searchingForm.length - 1:
        return '../../../assets/images/endMarker.png';
      default:
        return '../../../assets/images/viaMarker.png';
    }
  }

  private checkIfDeletedLocationIsDestination(index: number) {
    return this.searchingForm.length === index;
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

  private deleteMarker(index: number) {
    if (this.searchingForm.at(index).value.marker){
      removeMarker(this.searchingForm.at(index).value.marker);
      this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
    }
  }

  initializeWebSocketConnection() {
    const ws = new SockJS(environment.webSocketUrl);
    this.stompClient = Stomp.over(ws);
    let that = this;
    this.stompClient.connect({}, function () {
      that.openGlobalSocket();
    });
  }

  openGlobalSocket() {
    this.stompClient.subscribe(environment.publisherUrl + localStorage.getItem('email') + '/finish-driving', (message: { body: string }) => {
      const drivingNotificationDetails: SimpleDrivingInfo =  JSON.parse(message.body);
      this.activeRide = null;
      this.toast.info('Driving is finished.Tap to see details!')
        .onTap.subscribe(action => this.router.navigate(['/serb-uber/user/map-page-view', drivingNotificationDetails.drivingId]));
    });

    this.stompClient.subscribe(environment.publisherUrl + localStorage.getItem('email') + '/start-driving', (message: { body: string }) => {
      this.activeRide =  JSON.parse(message.body) as SimpleDrivingInfo;
      this.router.navigate(['/serb-uber/user/map-page-view', this.activeRide.drivingId]);
      this.toast.info('Ride started.Tap to follow ride!')
        .onTap.subscribe(action => this.router.navigate(['/serb-uber/user/map-page-view', this.activeRide.drivingId]));
    });

  }

  loadingView(loadingViewv:boolean) {
    this.loadingViewVar =loadingViewv;
    if (loadingViewv){
      this.filterVehicleView = false;
    }
  }
}
