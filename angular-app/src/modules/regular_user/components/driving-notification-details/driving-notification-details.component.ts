import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from 'src/modules/shared/models/user/user';
import { ConfigService } from 'src/modules/shared/services/config-service/config.service';
import { DrivingNotificationService } from 'src/modules/shared/services/driving-notification-service/driving-notification.service';
import { RouteService } from 'src/modules/shared/services/route-service/route.service';
import { UserService } from 'src/modules/shared/services/user-service/user.service';
import {
  drawAllMarkers,
  drawPolylineWithLngLatArray,
  removeAllMarkersFromList,
  removeLine
} from 'src/modules/shared/utils/map-functions';
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {ToastrService} from "ngx-toastr";
import {Vehicle} from "../../../shared/models/vehicle/vehicle";
import { AddDrivingNotification } from 'src/modules/shared/actions/driving-notification.action';
import { Store } from '@ngxs/store';
import {CurrentVehiclePosition} from "../../../shared/models/vehicle/current-vehicle-position";
import {DrivingNotificationResponse} from "../../../shared/models/notification/driving-notification-response";
import {DrivingNotification} from "../../../shared/models/notification/driving-notification";

@Component({
  selector: 'app-driving-notification-details',
  templateUrl: './driving-notification-details.component.html',
  styleUrls: ['./driving-notification-details.component.css']
})
export class DrivingNotificationDetailsComponent implements OnInit, OnDestroy {
  @Input() map: google.maps.Map;
  @Input() vehiclesCurrentPosition: CurrentVehiclePosition[];

  id: number;
  currentUser: User;
  base64Prefix = this.configService.BASE64_PHOTO_PREFIX;
  drivingNotification: DrivingNotificationResponse;
  routePolyline: google.maps.Polyline;
  markers: google.maps.Marker[];
  passengers: User[];
  answered: boolean;
  vehicle: Vehicle;
  drivingNotificationSubscription: Subscription;
  routeSubscription: Subscription;
  userSubscription: Subscription;
  authSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private drivingNotificationService: DrivingNotificationService,
    private router: Router,
    private routeService: RouteService,
    private configService: ConfigService,
    private userService: UserService,
    private authService: AuthService,
    private toastService: ToastrService,
    private store: Store
    ) {
      this.passengers = [];
      this.answered = false;
      this.vehicle = {
        vehicleTypeInfo: null
      }
  }

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id');
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.currentUser = user;
      });

    this.drivingNotificationSubscription = this.drivingNotificationService.getDrivingNotificationResponse(this.id).subscribe(
      (response) => {

        this.drivingNotification = response;
        this.vehicle = {
          vehicleTypeInfo: response.vehicleTypeInfo
        }
        this.passengers = response.passengers;
        if (this.map){
          this.routeSubscription = this.routeService.getRoutePath(this.drivingNotification?.route?.id).subscribe(path => {
              this.routePolyline = drawPolylineWithLngLatArray(this.map, path);
              this.markers = drawAllMarkers(this.drivingNotification?.route?.locations, this.map);
            }
          )
        }
      },
      error=>{
        console.log(error);
        this.drivingNotification = null;
      }
    )
  }

  acceptRide() {
    this.answered = true;
    this.drivingNotificationService.updateRideStatus(this.id, true, this.currentUser.email).subscribe(res => {
      console.log(res);
      this.toastService.success("Successfully accepted ride");
      const acceptedDriving: DrivingNotification = {
        route: this.drivingNotification.route,
        price: this.drivingNotification.price,
        started: this.drivingNotification.started,
        notificationId: this.id,
        drivingStatus: "PAYING",
        active: false,
        chosenDateTime: this.drivingNotification.chosenDateTime,
        reservation: this.drivingNotification.chosenDateTime !== null
      }
      this.store.dispatch(new AddDrivingNotification(acceptedDriving));
    }, err => this.toastService.error(err.error, "Accepting ride failed"));
  }

  rejectRide() {
    this.answered = true;
    this.drivingNotificationService.updateRideStatus(this.id, false, this.currentUser.email).subscribe(res => {
      console.log(res);
      this.toastService.success("Successfully rejected ride");
    }, err => this.toastService.error(err.error, "Rejecting ride failed"));
  }

  ngOnDestroy(): void {
    if (this.routePolyline){
      removeLine(this.routePolyline);
      removeAllMarkersFromList(this.markers);
    }

    if(this.drivingNotificationSubscription){
      this.drivingNotificationSubscription.unsubscribe();
    }

    if(this.routeSubscription){
      this.routeSubscription.unsubscribe();
    }

    if(this.userSubscription){
      this.userSubscription.unsubscribe();
    }

    if(this.authSubscription){
      this.authSubscription.unsubscribe();
    }
  }
}
