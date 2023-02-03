import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import {ConfigService} from "../../../shared/services/config-service/config.service";
import {Driving} from "../../../shared/models/driving/driving";
import {DrivingService} from "../../../shared/services/driving-service/driving.service";
import {RejectDrivingComponent} from "../reject-driving/reject-driving.component";
import { Select, Store } from '@ngxs/store';
import { DrivingNotificationState } from 'src/modules/shared/state/driving-notification.state';
import { AddDrivings } from 'src/modules/shared/actions/driving-notification.action';
import { ReportService } from 'src/modules/admin/services/report-service/report.service';
import { AuthService } from 'src/modules/auth/services/auth-service/auth.service';
import { User } from 'src/modules/shared/models/user/user';

@Component({
  selector: 'app-driver-home-container',
  templateUrl: './driver-home-page-container.component.html',
  styleUrls: ['./driver-home-page-container.component.css'],
})
export class DriverHomePageContainerComponent implements OnInit, OnDestroy {
  @Select(DrivingNotificationState.getDrivings) activeDrivings: Observable<Driving[]>;
  @Input() driverId: number;
  drivingSubscription: Subscription;
  nowAndFutureDrivings: Driving[];
  reasonForRejectingDriving: string;
  reportSubscription: Subscription;
  loggedUser: User;

  constructor(
    public configService: ConfigService,
    private drivingService: DrivingService,
    private router: Router,
    private dialog: MatDialog,
    private toast: ToastrService,
    private store: Store,
    private reportService: ReportService,
    private authService: AuthService
  ) {
    this.nowAndFutureDrivings = [];
    this.reasonForRejectingDriving = '';
  }

  ngOnInit(): void {
    this.activeDrivings.subscribe(response => {
      this.nowAndFutureDrivings = response;
    })
    this.drivingSubscription = this.drivingService
      .getDrivingsForDriver(this.driverId)
      .subscribe((drivings: Driving[]) => {
        this.store.dispatch(new AddDrivings(drivings));
      });

      this.authService.getSubjectCurrentUser().subscribe(
        res => {
          if (res) {this.loggedUser = res;}
        }
      );
  }

  finishDriving(drivingIndex: number): void {
    this.drivingSubscription = this.drivingService.finishDriving(this.nowAndFutureDrivings.at(drivingIndex).id).subscribe(
      res => {
        this.removeDriving(drivingIndex);
        this.toast.success("Successfully finished driving");
        console.log(res);
      },
      error => this.toast.error(error.error, 'Finishing driving failed')
    );
  }

  openRejectDrivingDialog(drivingIndex: number) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = {
      reasonForRejectingDriving: this.reasonForRejectingDriving,
    };
    const dialogRef = this.dialog.open(RejectDrivingComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(reason => {
      if (this.reasonEntered(reason)) {
        this.rejectDriving(this.nowAndFutureDrivings.at(drivingIndex).id, drivingIndex, reason);
      }
    });
  }

  startDriving(drivingIndex: number) {
    this.drivingSubscription = this.drivingService.startDriving(this.nowAndFutureDrivings.at(drivingIndex).id).subscribe(
      response => {
        this.nowAndFutureDrivings.at(drivingIndex).drivingStatus = response.drivingStatus;
        this.nowAndFutureDrivings.at(drivingIndex).active = response.active;
        this.nowAndFutureDrivings.at(drivingIndex).started = response.started;
        this.toast.success("Follow your current drive", "Successfully start driving");
      },
      error => this.toast.error(error.error, 'Starting driving failed')
    );
  }

  private reasonEntered(reason: string) {
    return reason !== '' && reason !== undefined;
  }

  private rejectDriving(drivingId: number, index: number, reason: string) {
    this.drivingSubscription = this.drivingService.rejectDriving(drivingId, reason).subscribe(
      response => {
        this.removeDriving(index);
        this.sendReport(drivingId, reason);
        this.toast.success("Successfully reject driving");
        console.log(response);
      },
      error => this.toast.error(error.error, 'Reject driving failed')
    );
  }

  private sendReport(drivingId: number, reason: string) {
    if (reason !== "I do not feel well" && reason !== "There is no passenger at the departure point" )
      this.reportService.createReportDriver(this.loggedUser.id, drivingId, reason).subscribe(
        res => {
          this.toast.success("Report is sent to admin.", 'Reported!');
          console.log(res);
        },
        err => {
          this.toast.error("Report cannot be sent.", 'Error occured!');
          console.log(err);
        }
    );
  }

  private removeDriving(drivingIndex: number): void {
    this.nowAndFutureDrivings.splice(drivingIndex, 1);
  }

  ngOnDestroy(): void {
    if (this.drivingSubscription) {
      this.drivingSubscription.unsubscribe();
    }

    if (this.reportSubscription) {
      this.reportSubscription.unsubscribe();
    }
  }


}
