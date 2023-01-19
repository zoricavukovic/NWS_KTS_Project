import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import {User} from "../../models/user/user";
import {Driving} from "../../models/driving/driving";
import {DrivingService} from "../../services/driving-service/driving.service";
import {Vehicle} from "../../models/vehicle/vehicle";
import {ConfigService} from "../../services/config-service/config.service";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {RouteService} from "../../services/route-service/route.service";
import {DriverService} from "../../services/driver-service/driver.service";
import {Driver} from "../../models/user/driver";
import {
  drawActiveRide,
  drawAllMarkers,
  drawPolylineWithLngLatArray,
  markCurrentPosition, removeAllMarkersFromList, removeLine
} from "../../utils/map-functions";
import { DrivingNotificationState } from '../../state/driving-notification.state';
import { DrivingNotification } from '../../models/notification/driving-notification';
import { Select } from '@ngxs/store';
import { RegularUserService } from '../../services/regular-user-service/regular-user.service';

@Component({
  selector: 'app-driving-details',
  templateUrl: './driving-details.component.html',
  styleUrls: [
    './driving-details.component.css',
    './driving-details.component.scss',
  ],
})
export class DrivingDetailsComponent implements OnInit, OnDestroy {
  @Select(DrivingNotificationState.getDrivingNotification) currentDrivingNotification: Observable<DrivingNotification>;
  storedDrivingNotification: DrivingNotification;
  @Input() map: google.maps.Map;
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

  currentUserSubscription: Subscription;
  drivingsSubscription: Subscription;
  driverSubscription: Subscription;
  vehicleRatingSubscription: Subscription;
  favouriteRouteSubscription: Subscription;
  vehicleSubscription: Subscription;
  routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private configService: ConfigService,
    private authService: AuthService,
    private drivingService: DrivingService,
    private driverService: DriverService,
    private regularUserService: RegularUserService,
    private routeService: RouteService,
    private router: Router,
  ) {
    this.activeRide = false;
    this.markers = [];
  }

  ngOnInit(): void {
    this.currentDrivingNotification.subscribe(response => {
      this.storedDrivingNotification = response;
    })
    this.router.events.subscribe((event) => {
      this.ngOnDestroy();
    });
    this.id = +this.route.snapshot.paramMap.get('id');
    this.destinations = [];
    this.drivingsSubscription = this.drivingService
      .get(this.id)
      .subscribe((driving: Driving) => {
        this.driving = driving;
        this.drivingsSubscription = this.drivingService.getVehicleDetails(driving?.id).subscribe(vehicleCurrentLocation => {
          markCurrentPosition(this.map, vehicleCurrentLocation);
        });

        if (this.map){
          this.routeSubscription = this.routeService.getRoutePath(driving?.route?.id).subscribe(path => {
            this.routePolyline = drawPolylineWithLngLatArray(this.map, path);
            if (driving.active){
              drawActiveRide(this.map, path, driving);
            }else{
              this.markers = drawAllMarkers(driving?.route?.locations, this.map);
            }
            }
          )
        }
        this.driverSubscription = this.driverService
          .get(driving?.driverId)
          .subscribe((response: Driver) => {
            this.driver = response;
          });

        this.currentUserSubscription = this.authService
          .getSubjectCurrentUser()
          .subscribe(user => {
            this.loggedUser = user;
            this.isRegularUser = this.authService.userIsRegular();
            this.isDriver = this.authService.userIsDriver();
          });

        this.favouriteRouteSubscription = this.regularUserService
          .isFavouriteRouteForUser(driving?.route?.id, this.loggedUser?.id)
          .subscribe(response => {
            if (response) {
              this.favouriteRoute = true;
            }
          });
      });
  }

  setFavouriteRoute(favourite: boolean) {
    if (favourite) {
      this.regularUserService
        .updateFavouriteRoutes(
          this.regularUserService.createFavouriteRequest(
            this.authService.getCurrentUserId,
            this.driving.route.id
          )
        )
        .subscribe(res => {
          this.favouriteRoute = false;
        });
    } else {
      this.regularUserService
        .addToFavouriteRoutes(
          this.regularUserService.createFavouriteRequest(
            this.authService.getCurrentUserId,
            this.driving.route.id
          )
        )
        .subscribe(res => {
          this.favouriteRoute = true;
        });
    }
  }

  canAcceptOrDeclineRide(): boolean{

    return this.router.url.includes("driving");
  }

  ngOnDestroy(): void {
    removeLine(this.routePolyline);
    removeAllMarkersFromList(this.markers);
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

    if(this.routeSubscription){
      this.routeSubscription.unsubscribe();
    }
  }
}
