import {Component, Input, OnDestroy, OnInit} from '@angular/core';
import {Observable, Subscription} from "rxjs";
import {ToastrService} from "ngx-toastr";
import {Options} from "ngx-google-places-autocomplete/objects/options/options";
import {Router} from "@angular/router";
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import {User} from "../../../shared/models/user/user";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {DrivingService} from "../../../shared/services/driving-service/driving.service";
import {RouteService} from "../../../shared/services/route-service/route.service";
import {VehicleService} from "../../../shared/services/vehicle-service/vehicle.service";
import {SearchingRoutesForm} from "../../../shared/models/route/searching-routes-form";
import {Route} from "../../../shared/models/route/route";
import {
  hideAllMarkers, removeAllPolyline,
} from "../../../shared/utils/map-functions";
import { DrivingNotificationState } from 'src/modules/shared/state/driving-notification.state';
import { Select, Store } from '@ngxs/store';
import { DrivingNotification } from 'src/modules/shared/models/notification/driving-notification';
import {UpdateDrivingNotification} from "../../../shared/actions/driving-notification.action";

@Component({
  selector: 'app-home-passanger',
  templateUrl: './home-passanger.component.html',
  styleUrls: ['./home-passanger.component.css']
})
export class HomePassangerComponent implements OnInit, OnDestroy {
  @Input() map: google.maps.Map;
  @Input() currentUser: User;
  @Select(DrivingNotificationState.getDrivingNotification) currentDrivingNotification: Observable<DrivingNotification>;
  storedDrivingNotification: DrivingNotification;

  routeChoiceView: boolean;
  filterVehicleView: boolean;
  requestLater: boolean;
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
  drawPolylineList: google.maps.Polyline[] = [];
  selectedRoute: Route;
  routePathIndex: number[] = [];
  loadingViewVar = false;

  authSubscription: Subscription;
  routeSubscription: Subscription;
  drivingSubscription: Subscription;
  rideIsRequested: boolean;

  options: Options = new Options({
    bounds: undefined,
    fields: ['address_component', 'formatted_address', 'name', 'geometry'],
    strictBounds: false,
    componentRestrictions: { country: 'rs' },
  });
  activeRide = null;

  constructor(
    private routeService: RouteService,
    private authService: AuthService,
    private vehicleService: VehicleService,
    private drivingService: DrivingService,
    private toast: ToastrService,
    public router: Router,
    private formBuilder: FormBuilder,
    private store: Store
  ){
    this.routeChoiceView = true;
    this.filterVehicleView = false;
    this.requestLater = false;
    this.rideIsRequested = false;
    this.rideRequestForm = new FormGroup({
      searchingRoutesForm: this.formBuilder.array([this.createEmptySearchForm(), this.createEmptySearchForm()]),
      selectedRoute: new FormControl(this.createEmptyRoute()),
      routePathIndex: new FormControl([]),
      petFriendly: new FormControl(false),
      babySeat: new FormControl(false),
      vehicleType: new FormControl(''),
      price: new FormControl(0),
      senderEmail: new FormControl(''),
      selectedPassengers: new FormControl([]),
      chosenDateTime: new FormControl(null)
    });
  }

  ngOnInit(): void {
    this.currentDrivingNotification.subscribe(response => {
      console.log(response);
      this.storedDrivingNotification = response;
      this.routeChoiceView = true;
      if (!response?.active) {
        this.activeRide = null;
      }
    });

    this.drivingSubscription = this.drivingService.checkIfUserHasActiveDriving(this.currentUser.id).subscribe(
      res => {
        this.activeRide = res;
        if(res){
          this.store.dispatch(new UpdateDrivingNotification(res)).subscribe()
        }
      }
    )
  }

  setView(enterLocations: boolean){
    if(enterLocations){
      this.routeChoiceView = true;
      this.filterVehicleView = false;
    }
  }


  ngOnDestroy(): void {
    hideAllMarkers(this.searchingForm);

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

  loadingView(loadingView:boolean): void {
    this.loadingViewVar = loadingView;
    if (loadingView){
      this.filterVehicleView = false;
    }
  }

  onRequestLater() {
    this.routeChoiceView = false;
    this.filterVehicleView = true;
    this.requestLater = true;
  }

  onRequestNow() {
    this.routeChoiceView = false;
    this.filterVehicleView = true;
    this.requestLater = false;
  }
}
