import { Component, Input, OnInit } from '@angular/core';
import {User} from "../../../shared/models/user/user";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {RouteService} from "../../../shared/services/route-service/route.service";
import {CurrentVehiclePosition} from "../../../shared/models/vehicle/current-vehicle-position";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit {
  @Input() map: google.maps.Map;
  @Input() currentUser: User;
  @Input() vehiclesCurrentPosition: CurrentVehiclePosition[];

  isDriver: boolean;
  isRegular: boolean;
  isAdmin: boolean;

  constructor(
    private routeService: RouteService,
    private authService: AuthService
  ) {
    this.isDriver = false;
    this.isRegular = false;
    this.isAdmin = false;
  }

  ngOnInit(): void {
    this.isDriver = this.authService.userIsDriver();
    this.isRegular = this.authService.userIsRegular();
    this.isAdmin = this.authService.userIsAdmin();
  }
}
