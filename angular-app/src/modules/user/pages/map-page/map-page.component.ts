import { Component, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
import { DrivingDetailsComponent } from '../../../shared/pages/driving-details/driving-details.component';
import { HomeComponent } from '../../components/home/home.component';
import { DrivingNotificationDetailsComponent } from '../../../regular_user/components/driving-notification-details/driving-notification-details.component';
import { CurrentVehiclePosition } from '../../../shared/models/vehicle/current-vehicle-position';
import {
  addCarMarker,
  hideMarker,
  removeMarker,
  updateVehiclePosition,
  visibleMarker,
} from '../../../shared/utils/map-functions';
import { environment } from '../../../../environments/environment';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { VehicleCurrentLocation } from '../../../shared/models/vehicle/vehicle-current-location';
import { VehicleService } from '../../../shared/services/vehicle-service/vehicle.service';
import { AuthService } from '../../../auth/services/auth-service/auth.service';
import { User } from '../../../shared/models/user/user';
import { Observable, Subscription } from 'rxjs';
import { Select, Store } from '@ngxs/store';
import { DrivingNotificationState } from '../../../shared/state/driving-notification.state';
import { DrivingNotification } from '../../../shared/models/notification/driving-notification';
import { updateTime } from '../../../shared/utils/time';
import { UpdateIfDriverChooseWrongRoute } from 'src/modules/shared/actions/driving-notification.action';

@Component({
  selector: 'map-page',
  templateUrl: './map-page.component.html',
  styleUrls: ['./map-page.component.css'],
})
export class MapPageComponent implements OnInit, OnDestroy {
  @ViewChild(DrivingDetailsComponent)
  private drivingDetailsComponent: DrivingDetailsComponent;
  @ViewChild(HomeComponent) private homeComponent: HomeComponent;
  @ViewChild(DrivingNotificationDetailsComponent)
  private drivingNotificationDetailsComponent: DrivingNotificationDetailsComponent;
  @Select(DrivingNotificationState.getDrivingNotification)
  currentDrivingNotification: Observable<DrivingNotification>;
  storedDrivingNotification: DrivingNotification;
  directionService: google.maps.DirectionsService;
  map: google.maps.Map;
  center: google.maps.LatLngLiteral = { lat: 45.25167, lng: 19.83694 };
  zoom = 13;
  vehiclesCurrentPosition: CurrentVehiclePosition[];
  stompClient;
  currentUser: User;
  authSubscription: Subscription;
  iterator: number;

  constructor(
    public router: Router,
    public actRoute: ActivatedRoute,
    private vehicleService: VehicleService,
    private authService: AuthService,
    private store: Store
  ) {
    this.vehiclesCurrentPosition = [];
    this.currentUser = null;
    this.iterator = 0;
    this.directionService = new google.maps.DirectionsService();
  }

  ngOnInit(): void {
    this.currentDrivingNotification.subscribe(
      response => (this.storedDrivingNotification = response)
    );
    this.initializeWebSocketConnection();
    this.router.events.subscribe(() => {
      this.checkIfShouldHideOrVisibleVehicles();
    });
    this.initMap();
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        if (user) {
          this.currentUser = user;
        }
      });
  }

  private initMap(): void {
    this.map = new google.maps.Map(
      document.getElementById('map') as HTMLElement,
      {
        center: this.center,
        mapId: '18f859a923044aa6',
        mapTypeId: google.maps.MapTypeId[Symbol.hasInstance],
        zoom: this.zoom,
        zoomControl: true,
        zoomControlOptions: {
          position: google.maps.ControlPosition.TOP_RIGHT,
        },
        streetViewControl: false,
      }
    );
    if (this.map) {
      this.initVehicles();
    }
  }

  private initVehicles() {
    this.vehicleService.getAllVehicle().subscribe(vehicleCurrentLocations => {
      vehicleCurrentLocations.forEach(vehicle => {
        const newVehicle: CurrentVehiclePosition = {
          vehicleCurrentLocation: vehicle,
          marker: addCarMarker(
            this.map,
            vehicle,
            this.currentUser?.id,
            this.storedDrivingNotification
          ),
        };
        this.vehiclesCurrentPosition.push(newVehicle);
      });
      if (this.actRoute.snapshot.paramMap.get('id') !== '-1') {
        if (this.vehiclesCurrentPosition) {
          this.vehiclesCurrentPosition.forEach(vehicle =>
            hideMarker(vehicle.marker)
          );
        }
      }
    });
  }

  private initializeWebSocketConnection() {
    const serverUrl = environment.webSocketUrl;
    const ws = new SockJS(serverUrl);
    this.stompClient = Stomp.over(ws);
    const that = this;

    this.stompClient.connect({}, function () {
      that.openGlobalSocket();
    });
  }

  private openGlobalSocket() {
    this.stompClient.subscribe(
      environment.publisherUrl + 'global/connect',
      message => {
        if (
          (message !== null && message !== undefined) ||
          message?.body !== null
        ) {
          const vehicleCurrentLocation: VehicleCurrentLocation = JSON.parse(
            message.body
          );
          const vehicle: CurrentVehiclePosition =
            this.vehiclesCurrentPosition.find(v => {
              return v.vehicleCurrentLocation.id === vehicleCurrentLocation.id;
            });
          this.iterator += 1;
          const index: number = this.vehiclesCurrentPosition.indexOf(vehicle);

          if (vehicle) {
            this.updateIfDriverChooseWrongRoute(vehicleCurrentLocation);
            if (!vehicleCurrentLocation.activeDriver) {
              vehicle.marker.setVisible(false);
              this.vehiclesCurrentPosition[index] = vehicle;
            } else {
              vehicle.marker = updateVehiclePosition(
                this.storedDrivingNotification,
                this.map,
                vehicle.marker,
                vehicleCurrentLocation,
                this.currentUser?.id,
                this.router.url.includes('-1')
              );
              this.iterator = updateTime(
                this.storedDrivingNotification,
                vehicle,
                this.directionService,
                this.store,
                this.iterator,
                this.authService.userIsAdmin()
              );
              vehicle.vehicleCurrentLocation = vehicleCurrentLocation;
              this.vehiclesCurrentPosition[index] = vehicle;
            }
          }

          if (!vehicle) {
            if (vehicleCurrentLocation.activeDriver) {
              const newVehicle: CurrentVehiclePosition = {
                vehicleCurrentLocation: vehicleCurrentLocation,
                marker: addCarMarker(
                  this.map,
                  vehicleCurrentLocation,
                  this.currentUser?.id,
                  this.storedDrivingNotification
                ),
              };

              this.iterator = updateTime(
                this.storedDrivingNotification,
                vehicle,
                this.directionService,
                this.store,
                this.iterator,
                this.authService.userIsAdmin()
              );
              this.vehiclesCurrentPosition.push(newVehicle);
            }
          }
        }
      }
    );
  }

  updateIfDriverChooseWrongRoute(
    vehicleCurrentLocation: VehicleCurrentLocation
  ) {
    const correctRouteIndex = this.storedDrivingNotification.route.locations.at(
      vehicleCurrentLocation.crossedWaypoints
    ).routeIndex;
    console.log(correctRouteIndex);
    console.log(vehicleCurrentLocation.chosenRouteIdx);
    console.log(vehicleCurrentLocation.id)
    if (
      correctRouteIndex !== vehicleCurrentLocation.chosenRouteIdx &&
      this.storedDrivingNotification.vehicleId === vehicleCurrentLocation.id &&
      !this.storedDrivingNotification.wrongRoute
    ) {
      console.log('trueee');
      this.store.dispatch(new UpdateIfDriverChooseWrongRoute(true));
    }
  }

  ngOnDestroy(): void {
    if (
      this.vehiclesCurrentPosition &&
      this.vehiclesCurrentPosition.length > 0
    ) {
      this.vehiclesCurrentPosition.forEach(vehicleCurrentPosition =>
        removeMarker(vehicleCurrentPosition.marker)
      );
    }

    if (this.map !== undefined) {
      this.map = null;
      window.location.reload();
    }

    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  private checkIfShouldHideOrVisibleVehicles() {
    if (this.actRoute.snapshot.paramMap.get('id') !== '-1') {
      this.vehiclesCurrentPosition.forEach(vehicle =>
        hideMarker(vehicle.marker)
      );
    } else {
      this.vehiclesCurrentPosition.forEach(vehicle =>
        visibleMarker(vehicle.marker)
      );
    }
  }
}
