import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { RouteService } from '../../../service/route.service';
import { User } from '../../../model/user/user';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../service/auth.service';
import {addCarMarkers, removeMarker} from "../../../util/map-functions";
import {VehicleService} from "../../../service/vehicle.service";

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements OnInit, OnDestroy {
  @Input() map: google.maps.Map;
  currentUser: User;
  isDriver: boolean;
  isRegular: boolean;
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
    this.carMarkers = [];
  }

  ngOnInit(): void {
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.currentUser = user;
        this.isDriver = this.authService.userIsDriver();
        this.isRegular = this.authService.userIsRegular();
        this.vehicleService.getAllVehicle().subscribe(vehicleCurrentLocation => {
          this.carMarkers = addCarMarkers(
            this.map,
            this.carMarkers,
            vehicleCurrentLocation,
            user.id
          );
        });
      });
  }

  ngOnDestroy(): void {
    this.carMarkers.forEach(marker => removeMarker(marker));

    if (this.authSubscription){
      this.authSubscription.unsubscribe();
    }
  }
}
