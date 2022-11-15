import { Driving } from 'src/app/model/response/driving';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ConfigService } from 'src/app/service/config.service';
import { AuthService } from 'src/app/service/auth.service';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/response/user/user';
import { Driver } from 'src/app/model/response/user/driver';
import { TooltipPosition } from '@angular/material/tooltip';
import { FavouriteRouteRequest } from 'src/app/model/request/favourite-route-request';
import { UserService } from 'src/app/service/user.service';
import { DrivingService } from 'src/app/service/driving.service';
import { DriverService } from 'src/app/service/driver.service';
import { Vehicle } from 'src/app/model/response/vehicle';

@Component({
  selector: 'app-driving-details',
  templateUrl: './driving-details.component.html',
  styleUrls: [
    './driving-details.component.css',
    './driving-details.component.scss',
  ],
})
export class DrivingDetailsComponent implements OnInit, OnDestroy {
  id: number;
  vehicleRating: number;
  driving: Driving = new Driving();
  driver: Driver;
  vehicle: Vehicle;
  currentUser: User;
  destinations: string[] = [];
  startPoint: string;
  favouriteRoute = false;
  positionOption: TooltipPosition = 'above';
  isDriver: boolean;
  isRegularUser: boolean;

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
    private driverService: DriverService
  ) {}

  ngOnInit(): void {
    this.id = +this.route.snapshot.paramMap.get('id');
    this.drivingsSubscription = this.drivingService
      .getDrivingDetails(this.id)
      .subscribe((response: Driving) => {
        this.driving = response;
        this.startPoint =
          this.driving.route.startPoint.street +
          ' ' +
          this.driving.route.startPoint.number;
        this.destinations.push(this.startPoint);
        for (const destination of this.driving.route.destinations) {
          this.destinations.push(destination.street + ' ' + destination.number);
        }

        this.driverSubscription = this.driverService
          .getDriver(this.driving.driverEmail)
          .subscribe((response: Driver) => {
            this.driver = response;
          });

        this.currentUserSubscription = this.authService
          .getCurrentUser()
          .subscribe(user => {
            this.isRegularUser = user.userIsRegular();
            this.isDriver = user.userIsDriver();
            this.currentUser = user;
            this.favouriteRouteSubscription = this.userService
              .isFavouriteRouteForUser(
                this.driving.route.id,
                this.currentUser.email
              )
              .subscribe(response => {
                if (response) {
                  this.favouriteRoute = true;
                }
              });
          });
      });
  }

  setFavouriteRoute() {
    if (this.favouriteRoute) {
      this.userService
        .removeFromFavouriteRoutes(
          new FavouriteRouteRequest(
            this.currentUser.email,
            this.driving.route.id
          )
        )
        .subscribe(response => {
          console.log(response);
          this.favouriteRoute = false;
        });
    } else {
      this.userService
        .addToFavouriteRoutes(
          new FavouriteRouteRequest(
            this.currentUser.email,
            this.driving.route.id
          )
        )
        .subscribe(response => {
          console.log(response);
          this.favouriteRoute = true;
        });
    }
  }

  getBase64Prefix(): string {
    return this.configService.base64_show_photo_prefix;
  }

  ngOnDestroy(): void {
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
