import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import {Subscription} from 'rxjs';
import {User} from "../../../shared/models/user/user";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {removeMarker} from "../../../shared/utils/map-functions";
import {RouteService} from "../../../shared/services/route-service/route.service";
import {VehicleService} from "../../../shared/services/vehicle-service/vehicle.service";
import {Router} from "@angular/router";
import {CurrentVehiclePosition} from "../../../shared/models/vehicle/current-vehicle-position";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit, OnDestroy {
  @Input() map: google.maps.Map;
  @Input() currentUser: User;
  @Input() vehiclesCurrentPosition: CurrentVehiclePosition[];

  isDriver: boolean;
  isRegular: boolean;
  isAdmin: boolean;

  constructor(
    private routeService: RouteService,
    private authService: AuthService,
    private vehicleService: VehicleService,
    private router: Router
  ) {
    this.isDriver = false;
    this.isRegular = false;
    this.isAdmin = false;
  }

  ngOnInit(): void {
    this.isDriver = this.authService.userIsDriver();
    this.isRegular = this.authService.userIsRegular();
    this.isAdmin = this.authService.userIsAdmin();
    this.router.events.subscribe(() => {
      this.ngOnDestroy();
    });
  }

  ngOnDestroy(): void {
    // if (this.vehiclesCurrentPosition && this.vehiclesCurrentPosition.length > 0){
    //   this.vehiclesCurrentPosition.forEach(vehicleCurrentPosition => removeMarker(vehicleCurrentPosition.marker));
    // }
  }
}
