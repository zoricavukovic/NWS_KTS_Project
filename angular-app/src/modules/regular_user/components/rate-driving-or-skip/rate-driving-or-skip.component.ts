import { Component, Input, OnInit } from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Select, Store } from '@ngxs/store';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subscription } from 'rxjs';
import {
  ClearStore,
  UpdateDrivingNotification,
} from 'src/modules/shared/actions/driving-notification.action';
import { RatingDialogComponent } from 'src/modules/shared/components/rating-dialog/rating-dialog.component';
import { SimpleDrivingInfo } from 'src/modules/shared/models/driving/simple-driving-info';
import { DrivingNotification } from 'src/modules/shared/models/notification/driving-notification';
import { User } from 'src/modules/shared/models/user/user';
import { DrivingService } from 'src/modules/shared/services/driving-service/driving.service';
import { DrivingNotificationState } from 'src/modules/shared/state/driving-notification.state';

@Component({
  selector: 'app-rate-driving-or-skip',
  templateUrl: './rate-driving-or-skip.component.html',
  styleUrls: ['./rate-driving-or-skip.component.css'],
})
export class RateDrivingOrSkipComponent implements OnInit {
  @Select(DrivingNotificationState.getDrivingNotification)
  currentDrivingNotification: Observable<DrivingNotification>;
  @Input() currentUser: User;
  storedDrivingNotification: DrivingNotification;
  constructor(
    private dialog: MatDialog,
    private toast: ToastrService,
    private store: Store
  ) {}

  goToDrivingDetails() {
    return `/serb-uber/user/map-page-view/${this.storedDrivingNotification.drivingId}`;
  }

  skipRate() {
    this.store.dispatch(new ClearStore());
  }

  rateDriving() {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.data = {
      drivingId: this.storedDrivingNotification.drivingId,
      userId: this.currentUser.id,
    };
    const dialogRef = this.dialog.open(RatingDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      response => {
        if (response !== null) {
          this.toast.success('Review created');
          this.store.dispatch(new ClearStore());
        }
      },
      error => {
        this.toast.error('Review creation failed');
      }
    );
  }

  ngOnInit() {
    this.currentDrivingNotification.subscribe(response => {
      this.storedDrivingNotification = response;
    });
  }
}
