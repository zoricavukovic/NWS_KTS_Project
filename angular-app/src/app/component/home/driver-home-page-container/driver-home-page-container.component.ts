import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Driving } from '../../../model/driving/driving';
import { Subscription } from 'rxjs';
import { DrivingService } from '../../../service/driving.service';
import { ConfigService } from 'src/app/service/config.service';
import {Router} from "@angular/router";
import {MatDialog, MatDialogConfig} from "@angular/material/dialog";
import {ToastrService} from "ngx-toastr";
import {RejectDrivingComponent} from "../../driving/reject-driving/reject-driving.component";


@Component({
  selector: 'app-driver-home-page-container',
  templateUrl: './driver-home-page-container.component.html',
  styleUrls: ['./driver-home-page-container.component.css']
})
export class DriverHomePageContainerComponent implements OnInit, OnDestroy {
  @Input() driverId: number;
  drivingSubscription: Subscription;
  nowAndFutureDrivings: Driving[] = [];
  maxNumberOfShowedUsers: number = 3;
  reasonForRejectingDriving: string = '';

  constructor(
    public configService: ConfigService,
    private drivingService: DrivingService,
    private router: Router,
    private dialog: MatDialog,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.drivingSubscription = this.drivingService
      .getDrivingsForDriver(this.driverId)
      .subscribe((drivings: Driving[]) => {
        this.nowAndFutureDrivings = drivings;
      });
  }

  ngOnDestroy(): void {
    if (this.drivingSubscription) {
      this.drivingSubscription.unsubscribe();
    }
  }

  getDrivingStartLocation(driving: Driving) {
    return `${driving.route.locations[0].location.street}, ${driving.route.locations[0].location.number}`;
  }

  getDrivingDestination(driving: Driving) {
    return `${
      driving.route.locations[driving.route.locations.length - 1].location
        .street
    }, ${
      driving.route.locations[driving.route.locations.length - 1].location
        .number
    }`;
  }

  endDrivingDate(startDate, duration) {
    const start = new Date(Date.parse(startDate));

    return new Date(start.getTime() + duration * 60000);
  }

  showOnMapDriving(driving: Driving) {}

  finishDriving(drivingId: number, drivingIndex: number) {

    this.drivingService.finishDriving(drivingId).subscribe(
      res => this.updateDrivingStatus(drivingIndex),
      error => this.toast.error(error.error, 'Finishing driving failed')
    )
  }

  getNumOfNotShowedUsers(driving: Driving): string {
    return `+${driving.users.length - this.maxNumberOfShowedUsers}`;
  }

  showDrivingDetails(drivingId: number): void {
    this.router.navigate([`/map-view/${drivingId}`]);
  }

  goToUserProfile(id: number): void {
    this.router.navigate([`/user-profile/${id}`]);
  }

  openRejectDrivingDialog(drivingId: number | undefined, index: number) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.data = {
      reasonForRejectingDriving: this.reasonForRejectingDriving
    };
    const dialogRef = this.dialog.open(RejectDrivingComponent, dialogConfig);


    dialogRef.afterClosed().subscribe(reason => {
      if (this.reasonEntered(reason)){
        this.rejectDriving(drivingId, index, reason)
      }
    })
  }

  startDriving(drivingId: number | undefined, index: number) {
    this.drivingService.startDriving(drivingId).subscribe(response =>
    {
      this.nowAndFutureDrivings.at(index).drivingStatus = "ACTIVE";
      this.nowAndFutureDrivings.at(index).active = true;
    }, error => this.toast.error(error.error, 'Starting driving failed'));
  }

  private reasonEntered(reason: string){
    return reason !== '' || reason !== undefined;
  }

  private rejectDriving(drivingId: number, index: number, reason: string) {
    this.drivingService.rejectDriving(drivingId, reason).subscribe(response =>
    {
      this.removeRejectedDriving(index);
    }, error => this.toast.error(error.error, 'Reject driving failed'));
  }

  private removeRejectedDriving(index: number): void {
    this.nowAndFutureDrivings.splice(index, 1);
  }

  private updateDrivingStatus(drivingIndex: number) {
    this.nowAndFutureDrivings.splice(drivingIndex, 1);
  }

}
