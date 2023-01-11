import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import {User} from "../../../models/user/user";
import {UserService} from "../../../services/user-service/user.service";
import {Driving} from "../../../models/driving/driving";
import {DrivingService} from "../../../services/driving-service/driving.service";
import {VehicleService} from "../../../services/vehicle-service/vehicle.service";
import {Vehicle} from "../../../models/vehicle/vehicle";
import {ConfigService} from "../../../services/config-service/config.service";
import {AuthService} from "../../../../auth/services/auth-service/auth.service";
import {RouteService} from "../../../services/route-service/route.service";
import {DriverService} from "../../../services/driver-service/driver.service";
import {Driver} from "../../../models/user/driver";
import {
  drawActiveRide,
  drawAllMarkers,
  drawPolylineWithLngLatArray,
  markCurrentPosition, removeAllMarkersFromList, removeLine
} from "../../../utils/map-functions";

@Component({
  selector: 'app-driving-details',
  templateUrl: './driving-details.component.html',
  styleUrls: [
    './driving-details.component.css',
    './driving-details.component.scss',
  ],
})
export class DrivingDetailsComponent implements OnInit, OnDestroy {
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

  constructor(
    private route: ActivatedRoute,
    private configService: ConfigService,
    private authService: AuthService,
    private userService: UserService,
    private drivingService: DrivingService,
    private driverService: DriverService,
    private routeService: RouteService,
    private router: Router,
    private vehicleService: VehicleService
  ) {
    this.activeRide = false;
    this.markers = [];
  }

  ngOnInit(): void {
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
          this.routeService.getRoutePath(driving?.route?.id).subscribe(path => {
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

        this.favouriteRouteSubscription = this.userService
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
      this.userService
        .updateFavouriteRoutes(
          this.userService.createFavouriteRequest(
            this.authService.getCurrentUserId,
            this.driving.route.id
          )
        )
        .subscribe(res => {
          this.favouriteRoute = false;
        });
    } else {
      this.userService
        .addToFavouriteRoutes(
          this.userService.createFavouriteRequest(
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
  }
}
