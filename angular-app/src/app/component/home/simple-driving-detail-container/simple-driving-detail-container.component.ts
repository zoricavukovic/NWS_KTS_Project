import {Component, EventEmitter, Input, Output} from '@angular/core';
import {Driving} from "../../../model/driving/driving";
import {ConfigService} from "../../../service/config.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-simple-driving-detail-container',
  templateUrl: './simple-driving-detail-container.component.html',
  styleUrls: ['./simple-driving-detail-container.component.css']
})
export class SimpleDrivingDetailContainerComponent {

  @Input() driving: Driving;
  @Input() index: number;
  @Output() finishDriving = new EventEmitter();
  @Output() startDriving = new EventEmitter();
  @Output() rejectDriving = new EventEmitter();

  maxNumberOfShowedUsers: number;

  constructor(public configService: ConfigService,  private router: Router) {
    this.maxNumberOfShowedUsers = 3;
  }

  getDrivingStartLocation(driving: Driving) {
    return `${driving.route.locations[0].location.street}, ${driving.route.locations[0].location.number}`;
  }

  getDrivingDestination(driving: Driving) {
    return `${
      driving.route.locations[driving.route.locations.length - 1].location?.street
    }, ${
      driving.route.locations[driving.route.locations.length - 1].location?.number
    }`;
  }

  endDrivingDate(startDate, duration) {
    const start = new Date(Date.parse(startDate));

    return new Date(start.getTime() + duration * 60000);
  }

  showDrivingDetails(drivingId: number): void {
    this.router.navigate([`/map-view/${drivingId}`]);
  }

  goToUserProfile(id: number): void {
    this.router.navigate([`/user-profile/${id}`]);
  }

  getNumOfNotShowedUsers(driving: Driving): string {
    return `+${driving.users.length - this.maxNumberOfShowedUsers}`;
  }

  onFinishDriving():void {
    this.finishDriving.emit();
  }

  showOnMapDriving(driving: Driving):void {
    console.log("prikaz", driving);
  }

  onStartDriving():void {
    this.startDriving.emit();
  }

  onRejectDriving():void {
    this.rejectDriving.emit();
  }
}
