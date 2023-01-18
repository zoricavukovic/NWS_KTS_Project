import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import {User} from "../../../shared/models/user/user";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {addCarMarkers, removeMarker} from "../../../shared/utils/map-functions";
import {RouteService} from "../../../shared/services/route-service/route.service";
import {VehicleService} from "../../../shared/services/vehicle-service/vehicle.service";

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css'],
})
export class HomeComponent implements OnInit, OnDestroy {
  @Input() map: google.maps.Map;
  currentUser: User;
  isDriver: boolean;
  isRegular: boolean;
  isAdmin: boolean;
  carMarkers: google.maps.Marker[];

  authSubscription: Subscription;

  constructor(
    private routeService: RouteService,
    private authService: AuthService,
    private vehicleService: VehicleService
  ) {
    this.currentUser = null;
    this.isDriver = false;
    this.isRegular = false;
    this.isAdmin = false;
    this.carMarkers = [];
  }

  ngOnInit(): void {
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        if(user){
          this.currentUser = user;
          this.isDriver = this.authService.userIsDriver();
          this.isRegular = this.authService.userIsRegular();
          this.isAdmin = this.authService.userIsAdmin();
          this.vehicleService.getAllVehicle().subscribe(vehicleCurrentLocation => {
            this.carMarkers = addCarMarkers(
              this.map,
              this.carMarkers,
              vehicleCurrentLocation,
              user.id
            );
          });
      }
      });
  }

  ngOnDestroy(): void {
    this.carMarkers.forEach(marker => removeMarker(marker));

    if (this.authSubscription){
      this.authSubscription.unsubscribe();
    }
  }
}
