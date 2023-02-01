import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { User } from '../../models/user/user';
import { Driving } from '../../models/driving/driving';
import { DrivingService } from '../../services/driving-service/driving.service';
import { Vehicle } from '../../models/vehicle/vehicle';
import { ConfigService } from '../../services/config-service/config.service';
import { AuthService } from '../../../auth/services/auth-service/auth.service';
import { RouteService } from '../../services/route-service/route.service';
import { DriverService } from '../../services/driver-service/driver.service';
import { Driver } from '../../models/user/driver';
import { Route as RouteLocation } from '../../models/route/route';
import {
  drawActiveRide,
  drawPolylineWithLngLatArray,
  hideMarker,
  removeAllMarkersFromList,
  removeLine,
  visibleMarker,
} from '../../utils/map-functions';
import { DrivingNotificationState } from '../../state/driving-notification.state';
import { DrivingNotification } from '../../models/notification/driving-notification';
import { Select, Store } from '@ngxs/store';
import { RegularUserService } from '../../services/regular-user-service/regular-user.service';

import { CurrentVehiclePosition } from '../../models/vehicle/current-vehicle-position';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';
import { getTime } from '../../utils/time';
import { BehaviourReportDialogComponent } from '../../components/behaviour-report-dialog/behaviour-report-dialog.component';
import { MatDialog } from '@angular/material/dialog';
import {
  AddDrivingNotification,
} from '../../actions/driving-notification.action';

@Component({
  selector: 'app-driving-details',
  templateUrl: './driving-details.component.html',
  styleUrls: [
    './driving-details.component.css',
    './driving-details.component.scss',
  ],
})
export class DrivingDetailsComponent implements OnInit, OnDestroy {
  @Select(DrivingNotificationState.getDrivingNotification)
  currentDrivingNotification: Observable<DrivingNotification>;
  storedDrivingNotification: DrivingNotification;
  @Input() map: google.maps.Map;
  @Input() vehiclesCurrentPosition: CurrentVehiclePosition[];
  indexOfVehicleForDriving: number;
  directionService: google.maps.DirectionsService;
  id: number;
  driving: Driving;
  driver: Driver;
  vehicle: Vehicle;
  destinations: string[] = [];
  favouriteRoute = false;
  isDriver: boolean;
  isRegularUser: boolean;
  routePolyline: google.maps.Polyline;
  markers: google.maps.Marker[];
  loggedUser: User = null;
  base64Prefix = this.configService.BASE64_PHOTO_PREFIX;
  activeRide: boolean;
  rideRequestForm: FormGroup;
  filterVehicleView = false;
  requestLater = false;

  currentUserSubscription: Subscription;
  drivingsSubscription: Subscription;
  driverSubscription: Subscription;
  vehicleRatingSubscription: Subscription;
  favouriteRouteSubscription: Subscription;
  routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private configService: ConfigService,
    private authService: AuthService,
    private drivingService: DrivingService,
    private driverService: DriverService,
    private regularUserService: RegularUserService,
    private routeService: RouteService,
    public router: Router,
    private formBuilder: FormBuilder,
    private store: Store,
    private _dialogRef: MatDialog
  ) {
    this.activeRide = false;
    this.markers = [];
    this.directionService = new google.maps.DirectionsService();
    this.rideRequestForm = new FormGroup({
      searchingRoutesForm: this.formBuilder.array([]),
      selectedRoute: new FormControl(null),
      routePathIndex: new FormControl([]),
      petFriendly: new FormControl(false),
      babySeat: new FormControl(false),
      vehicleType: new FormControl(''),
      price: new FormControl(0),
      senderEmail: new FormControl(''),
      selectedPassengers: new FormControl([]),
      chosenDateTime: new FormControl(null),
    });
  }

  ngOnInit(): void {
    this.currentDrivingNotification.subscribe(response => {
      this.storedDrivingNotification = response;
      if (this.storedDrivingNotification?.drivingStatus === 'PAYING') {
        this.filterVehicleView = false;
      }
    });
    if (this.route.snapshot.paramMap.get('id') !== '-1') {
      this.vehiclesCurrentPosition.forEach(vehicle =>
        hideMarker(vehicle.marker)
      );
    } else {
      this.vehiclesCurrentPosition.forEach(vehicle =>
        visibleMarker(vehicle.marker)
      );
    }
    this.router.events.subscribe(() => {
      this.ngOnDestroy();
    });
    this.id = +this.route.snapshot.paramMap.get('id');
    this.destinations = [];
    this.drivingsSubscription = this.drivingService
      .get(this.id)
      .subscribe((driving: Driving) => {
        this.driving = driving;
        if (this.storedDrivingNotification === null) {
          this.store.dispatch(
            new AddDrivingNotification({
              drivingId: driving.id,
              drivingStatus: driving.drivingStatus,
              price: driving.price,
              active: driving.active,
              route: driving.route,
              started: driving.started,
              wrongRoute: false,
            })
          );
        }
        driving.route.routePathIndex = this.getRoutePathIndex(driving.route);
        this.rideRequestForm.get('selectedRoute').setValue(driving.route);

        if (this.map) {
          this.routeSubscription = this.routeService
            .getRoutePath(driving?.route?.id)
            .subscribe(path => {
              this.routePolyline = drawPolylineWithLngLatArray(this.map, path);
            });
        }

        this.driverSubscription = this.driverService
          .get(driving?.driverId)
          .subscribe((driver: Driver) => {
            this.driver = driver;
            const vehicle: CurrentVehiclePosition =
              this.vehiclesCurrentPosition.find(v => {
                return v.vehicleCurrentLocation.id === driver.vehicle.id;
              });
            this.indexOfVehicleForDriving =
              this.vehiclesCurrentPosition.indexOf(vehicle);
            this.markers = drawActiveRide(
              this.map,
              driving,
              driver,
              vehicle,
              this.indexOfVehicleForDriving,
              this.directionService,
              this.store,
              this.authService.userIsAdmin(),
              this.storedDrivingNotification
            );
          });

        this.currentUserSubscription = this.authService
          .getSubjectCurrentUser()
          .subscribe(user => {
            this.loggedUser = user;
            this.isRegularUser = this.authService.userIsRegular();
            this.isDriver = this.authService.userIsDriver();
          });
      });
  }

  reportDriveBehaviour() {
    if (this.loggedUser) {
      this._dialogRef.open(BehaviourReportDialogComponent, {
        data: {
          currentUser: this.loggedUser,
          driver: this.driver,
          userToReport: null,
        },
      });
    }
  }

  showFilterVehicleView(requestLater: boolean) {
    if (requestLater) {
      this.filterVehicleView = true;
      this.requestLater = true;
    } else {
      this.filterVehicleView = true;
    }
  }

  getRoutePathIndex(route: RouteLocation): number[] {
    const routePathIndex = [];
    route.locations.forEach(element => {
      routePathIndex.push(element.routeIndex);
    });

    return routePathIndex;
  }

  canAcceptOrDeclineRide(): boolean {
    return this.router.url.includes('driving');
  }

  ngOnDestroy(): void {
    if (this.route.snapshot.paramMap.get('id') !== '-1') {
      this.vehiclesCurrentPosition.forEach(vehicle =>
        hideMarker(vehicle.marker)
      );
    } else {
      removeLine(this.routePolyline);
      removeAllMarkersFromList(this.markers);
      this.vehiclesCurrentPosition.forEach(vehicle =>
        visibleMarker(vehicle.marker)
      );
    }

    if (this.currentUserSubscription) {
      this.currentUserSubscription.unsubscribe();
    }
    if (this.drivingsSubscription) {
      this.drivingsSubscription.unsubscribe();
    }

    if (this.driverSubscription) {
      this.driverSubscription.unsubscribe();
    }

    if (this.favouriteRouteSubscription) {
      this.favouriteRouteSubscription.unsubscribe();
    }

    if (this.vehicleRatingSubscription) {
      this.vehicleRatingSubscription.unsubscribe();
    }

    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }

  getTime(): string {
    return getTime(this.storedDrivingNotification);
  }
}
