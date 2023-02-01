import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, Subscription } from 'rxjs';
import { DrivingService } from 'src/modules/shared/services/driving-service/driving.service';
import { SimpleDrivingInfo } from '../../../shared/models/driving/simple-driving-info';
import { UpdateDrivingNotification } from '../../../shared/actions/driving-notification.action';
import { Select, Store } from '@ngxs/store';
import { DrivingNotification } from 'src/modules/shared/models/notification/driving-notification';
import { DrivingNotificationState } from 'src/modules/shared/state/driving-notification.state';
import { getTime } from '../../../shared/utils/time';

@Component({
  selector: 'app-active-drive-container',
  templateUrl: './active-drive-container.component.html',
  styleUrls: ['./active-drive-container.component.css'],
})
export class ActiveDriveContainerComponent implements OnInit, OnDestroy {
  @Select(DrivingNotificationState.getDrivingNotification)
  currentDrivingNotification: Observable<DrivingNotification>;
  storedDrivingNotification: DrivingNotification;
  @Input() currentUserId: number;
  activeRide: SimpleDrivingInfo;
  drivingSubscription: Subscription;
  drivingTimeSubscription: Subscription;
  constructor(
    private _router: Router,
    private _drivingService: DrivingService,
    private store: Store
  ) {}

  goToDrivingDetails() {
    this._router.navigate([
      '/serb-uber/user/map-page-view',
      this.storedDrivingNotification.drivingId,
    ]);
  }

  ngOnInit() {
    this.currentDrivingNotification.subscribe(response => {
      this.storedDrivingNotification = response;
    });
    this.drivingSubscription = this._drivingService
      .checkIfUserHasActiveDriving(this.currentUserId)
      .subscribe(driving => {
        this.activeRide = driving;
        if (driving) {
          this.drivingTimeSubscription = this._drivingService
            .getTimeForDriving(driving.drivingId)
            .subscribe(time => {
              driving.started = time;
              this.store
                .dispatch(new UpdateDrivingNotification(driving))
                .subscribe();
            });
        }
      });
  }

  getTime(): string {
    return getTime(this.storedDrivingNotification);
  }

  checkIfDrivingIsOnDepartureType(): boolean {
    return (
      this.storedDrivingNotification?.active &&
      this.storedDrivingNotification.drivingStatus === 'ON_WAY_TO_DEPARTURE'
    );
  }

  checkIfVehicleArrive(): boolean {
    return (
      !this.storedDrivingNotification?.active &&
      this.storedDrivingNotification.drivingStatus === 'ON_WAY_TO_DEPARTURE'
    );
  }

  checkIfDrivingIsActive(): boolean {
    return (
      this.storedDrivingNotification?.active &&
      this.storedDrivingNotification.drivingStatus === 'ACCEPTED'
    );
  }

  checkIfDrivingIsFuture(): boolean {
    return (
      !this.storedDrivingNotification?.active &&
      this.storedDrivingNotification.drivingStatus === 'ACCEPTED'
    );
  }

  ngOnDestroy(): void {
    if (this.drivingSubscription) {
      this.drivingSubscription.unsubscribe();
    }
    if (this.drivingTimeSubscription) {
      this.drivingTimeSubscription.unsubscribe();
    }
  }
}
