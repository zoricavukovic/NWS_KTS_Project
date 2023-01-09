import { Driving } from 'src/app/model/driving/driving';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ConfigService } from 'src/app/service/config.service';
import { AuthService } from 'src/app/service/auth.service';
import { Subscription } from 'rxjs';
import { Driver } from 'src/app/model/user/driver';
import { UserService } from 'src/app/service/user.service';
import { DrivingService } from 'src/app/service/driving.service';
import { DriverService } from 'src/app/service/driver.service';
import { Vehicle } from 'src/app/model/vehicle/vehicle';
import { User } from 'src/app/model/user/user';
import {
  drawAllMarkers,
  drawPolylineWithLngLatArray,
  removeAllMarkers, removeAllMarkersFromList,
  removeLine
} from '../../../../util/map-functions';
import {RouteService} from "../../../../service/route.service";

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
  vehicleRating: number;
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
  base64Prefix = this.configService.base64_show_photo_prefix;

  currentUserSubscription: Subscription;
  drivingsSubscription: Subscription;
  driverSubscription: Subscription;
  vehicleRatingSubscription: Subscription;
  favouriteRouteSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private configService: ConfigService,
    private authService: AuthService,
    private userService: UserService,
    private drivingService: DrivingService,
    private driverService: DriverService,
    private routeService: RouteService,
    private router: Router
  ) {}

  ngOnInit(): void {
    console.log(document.getElementById("pera"));
    this.router.events.subscribe((event) => {
      this.ngOnDestroy();
    });
    this.id = +this.route.snapshot.paramMap.get('id');
    this.destinations = [];
    this.drivingsSubscription = this.drivingService
      .get(this.id)
      .subscribe((driving: Driving) => {
        console.log(driving);
        this.driving = driving;
        if (this.map){
          this.routeService.getRoutePath(driving?.route?.id).subscribe(path => {
              this.markers = drawAllMarkers(driving?.route?.locations, this.map);
              this.routePolyline = drawPolylineWithLngLatArray(this.map, path);
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
    console.log(favourite);
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
