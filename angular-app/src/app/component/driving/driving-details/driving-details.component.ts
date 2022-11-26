import { Driving } from 'src/app/model/driving/driving';
import {
  AfterViewInit,
  Component,
  Input,
  OnDestroy,
  OnInit,
} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ConfigService } from 'src/app/service/config.service';
import { AuthService } from 'src/app/service/auth.service';
import { Subscription } from 'rxjs';
import { Driver } from 'src/app/model/user/driver';
import { UserService } from 'src/app/service/user.service';
import { DrivingService } from 'src/app/service/driving.service';
import { DriverService } from 'src/app/service/driver.service';
import {drawPolyline, refreshMap, removeSpecificPolyline} from '../../../util/map-functions';
import { Vehicle } from 'src/app/model/vehicle/vehicle';
import * as L from 'leaflet';

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
  favouriteRoute: boolean = false;
  isDriver: boolean;
  isRegularUser: boolean;
  routePolyline: L.Polyline;

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

        this.isRegularUser = this.authService.getCurrentUser?.userIsRegular();
        this.isDriver = this.authService.getCurrentUser?.userIsDriver();

        this.favouriteRouteSubscription = this.userService
          .isFavouriteRouteForUser(
            driving?.route?.id,
            this.authService.getCurrentUser?.id
          )
          .subscribe(response => {
            if (response) {
              this.favouriteRoute = true;
            }
          });
      });
  }

  setFavouriteRoute() {
    if (this.favouriteRoute) {
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

  getBase64Prefix(): string {
    return this.configService.base64_show_photo_prefix;
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
