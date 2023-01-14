import {Component, EventEmitter, Input, OnDestroy, OnInit, Output} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import { Select } from '@ngxs/store';
import {Observable, Subscription} from "rxjs";
import { DrivingNotification } from 'src/modules/shared/models/notification/driving-notification';
import { User } from 'src/modules/shared/models/user/user';
import { DrivingNotificationState } from 'src/modules/shared/state/driving-notification.state';
import {AuthService} from "../../../../auth/services/auth-service/auth.service";
import {DrivingNotificationService} from "../../../services/driving-notification-service/driving-notification.service";

@Component({
  selector: 'app-driving-details-actions',
  templateUrl: './driving-details-actions.component.html',
  styleUrls: ['./driving-details-actions.component.css'],
})
export class DrivingDetailsActionsComponent implements OnInit, OnDestroy {
  @Select(DrivingNotificationState.getDrivingNotification) currentDrivingNotification: Observable<DrivingNotification>;
  storedDrivingNotification: DrivingNotification;
  @Input() favouriteRoute: boolean;
  @Output() setFavouriteRouteEvent = new EventEmitter<boolean>();

  currentUser: User;

  authSubscription: Subscription;
  drivingId: number;

  constructor(
    private _authService: AuthService,
    private _activeRoute: ActivatedRoute,
    private _router: Router,
    private _drivingNotificationService: DrivingNotificationService
  ) {
    this.drivingId = -1;
  }

  ngOnInit(): void {
    this.currentDrivingNotification.subscribe(response => {
      this.storedDrivingNotification = response;
    })
    this.drivingId = +this._activeRoute.snapshot.paramMap.get('id');
    this.authSubscription = this._authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.currentUser = user;
      });
  }

  setFavouriteRouteEmitter() {
    this.setFavouriteRouteEvent.emit(this.favouriteRoute);
  }

  canAcceptOrDeclineRide(): boolean{

    return this._router.url.includes("driving");
  }

  acceptRide() {
    this._drivingNotificationService.updateRideStatus(this.drivingId, true, this.currentUser.email).subscribe(res => {
      console.log(res);
    });
  }

  declineRide() {
    this._drivingNotificationService.updateRideStatus(this.drivingId, false, this.currentUser.email).subscribe(res => {
      console.log(res);
    });
  }

  ngOnDestroy(): void {
    if (this.authSubscription){
      this.authSubscription.unsubscribe();
    }
  }
}
