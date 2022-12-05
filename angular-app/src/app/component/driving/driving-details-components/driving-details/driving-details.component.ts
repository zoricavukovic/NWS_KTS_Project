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
import {
  drawPolyline,
  removeSpecificPolyline,
} from 'src/app/util/map-functions';
import { Vehicle } from 'src/app/model/vehicle/vehicle';
import * as L from 'leaflet';
import { User } from 'src/app/model/user/user';

@Component({
  selector: 'app-driving-details',
  templateUrl: './driving-details.component.html',
  styleUrls: [
    './driving-details.component.css',
    './driving-details.component.scss',
  ],
})
export class DrivingDetailsComponent implements OnInit, OnDestroy {
  @Input() map;
  id: number;
  vehicleRating: number;
  driving: Driving;
  driver: Driver;
  vehicle: Vehicle;
  destinations: string[] = [];
  favouriteRoute = false;
  isDriver: boolean;
  isRegularUser: boolean;
  routePolyline: L.Polyline;
  loggedUser: User = null;
  base64Prefix = this.configService.base64_show_photo_prefix;

  currentUserSubscription: Subscription;
  drivingsSubscription: Subscription;
  driverSubscription: Subscription;
  vehicleRatingSubscription: Subscription;
  favouriteRouteSubscription: Subscription;

  vehicle_image = {
    VAN: '/assets/images/van.png',
    SUV: '/assets/images/suv.png',
    CAR: '/assets/images/car.png',
  };

  constructor(
    private route: ActivatedRoute,
    private configService: ConfigService,
    private authService: AuthService,
    private userService: UserService,
    private drivingService: DrivingService,
    private driverService: DriverService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id');
    this.destinations = [];
    this.drivingsSubscription = this.drivingService
      .getDrivingDetails(this.id)
      .subscribe((driving: Driving) => {
        this.driving = driving;
        if (this.map) {
          this.routePolyline = drawPolyline(this.map, this.driving?.route);
        }

        this.driverSubscription = this.driverService
          .getDriver(driving?.driverId)
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

    const div = L.DomUtil.get('route-div');
    L.DomEvent.on(div, 'mousewheel', L.DomEvent.stopPropagation);
    L.DomEvent.on(div, 'click', L.DomEvent.stopPropagation);
    L.DomEvent.disableScrollPropagation(document.getElementById('route-div'));
  }

  setFavouriteRoute(favourite: boolean) {
    if (favourite) {
      this.userService
        .removeFromFavouriteRoutes(
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

  ngOnDestroy(): void {
    removeSpecificPolyline(this.map, this.routePolyline);
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
