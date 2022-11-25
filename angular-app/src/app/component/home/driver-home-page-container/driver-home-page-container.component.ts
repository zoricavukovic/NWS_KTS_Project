import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Driving } from '../../../model/driving/driving';
import { Subscription } from 'rxjs';
import { DrivingService } from '../../../service/driving.service';

@Component({
  selector: 'app-driver-home-page-container',
  templateUrl: './driver-home-page-container.component.html',
  styleUrls: ['./driver-home-page-container.component.css'],
})
export class DriverHomePageContainerComponent implements OnInit, OnDestroy {
  @Input() driverId: number;
  drivingSubscription: Subscription;
  nowAndFutureDrivings: Driving[] = [];
  maxNumberOfShowedUsers: number = 3;
  constructor(private drivingService: DrivingService) {}

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
      err => console.log(err)
    )
  }

  getNumOfNotShowedUsers(driving: Driving): string {
    return `+${driving.users.length - this.maxNumberOfShowedUsers}`;
  }

  showDrivingDetails(drivingId: number): void {}

  hasNotFutureDrivings(): boolean {
    return (
      this.nowAndFutureDrivings.length === 0 ||
      (this.nowAndFutureDrivings.length === 1 &&
        this.nowAndFutureDrivings.at(0).active)
    );
  }

  hasNoActiveDriving(driving: Driving): boolean {
    return !driving.active;
  }

  private updateDrivingStatus(drivingIndex: number) {
    this.nowAndFutureDrivings.splice(drivingIndex, 1);
  }
}