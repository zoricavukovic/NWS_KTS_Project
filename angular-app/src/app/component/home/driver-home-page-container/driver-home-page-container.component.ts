import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Driving } from '../../../model/driving/driving';
import { Subscription } from 'rxjs';
import { DrivingService } from '../../../service/driving.service';
import { ConfigService } from 'src/app/service/config.service';
import { Router } from '@angular/router';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { RejectDrivingComponent } from '../../driving/reject-driving/reject-driving.component';

@Component({
  selector: 'app-driver-home-page-container',
  templateUrl: './driver-home-page-container.component.html',
  styleUrls: ['./driver-home-page-container.component.css'],
})
export class DriverHomePageContainerComponent implements OnInit, OnDestroy {
  @Input() driverId: number;
  drivingSubscription: Subscription;
  nowAndFutureDrivings: Driving[];
  reasonForRejectingDriving: string;

  constructor(
    public configService: ConfigService,
    private drivingService: DrivingService,
    private router: Router,
    private dialog: MatDialog,
    private toast: ToastrService
  ) {
    this.nowAndFutureDrivings = [];
    this.reasonForRejectingDriving = '';
  }

  ngOnInit(): void {
    this.drivingSubscription = this.drivingService
      .getDrivingsForDriver(this.driverId)
      .subscribe((drivings: Driving[]) => {
        console.log(drivings);
        this.nowAndFutureDrivings = drivings;
      });
  }

  ngOnDestroy(): void {
    if (this.drivingSubscription) {
      this.drivingSubscription.unsubscribe();
    }
  }

  finishDriving(drivingIndex: number): void {
    this.drivingSubscription = this.drivingService.finishDriving(this.nowAndFutureDrivings.at(drivingIndex).id).subscribe(
      res => {
        this.removeDriving(drivingIndex);
        this.toast.success("Successfully finished driving");
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
        this.toast.success("Successfully reject driving");
      },
      error => this.toast.error(error.error, 'Reject driving failed')
    );
  }

  private removeDriving(drivingIndex: number): void {
    this.nowAndFutureDrivings.splice(drivingIndex, 1);
  }
}
