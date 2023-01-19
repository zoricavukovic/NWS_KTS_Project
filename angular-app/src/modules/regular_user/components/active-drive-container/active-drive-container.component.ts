import { Component, Input, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { Observable, Subscription } from 'rxjs';
import { DrivingService } from 'src/modules/shared/services/driving-service/driving.service';
import {SimpleDrivingInfo} from "../../../shared/models/driving/simple-driving-info";
import {UpdateDrivingNotification} from "../../../shared/actions/driving-notification.action";
import {Select, Store} from "@ngxs/store";
import { DrivingNotification } from 'src/modules/shared/models/notification/driving-notification';
import { DrivingNotificationState } from 'src/modules/shared/state/driving-notification.state';

@Component({
  selector: 'app-active-drive-container',
  templateUrl: './active-drive-container.component.html',
  styleUrls: ['./active-drive-container.component.css']
})
export class ActiveDriveContainerComponent implements OnInit {
  @Select(DrivingNotificationState.getDrivingNotification) currentDrivingNotification: Observable<DrivingNotification>;
  storedDrivingNotification: DrivingNotification;
  @Input() currentUserId: number;
  activeRide: SimpleDrivingInfo;
  drivingSubscription: Subscription;
  constructor(private _router: Router, private _drivingService: DrivingService, private store: Store) { }

  goToDrivingDetails() {
    this._router.navigate(['/serb-uber/user/map-page-view', this.storedDrivingNotification.drivingId]);
  }

  ngOnInit(){
    this.currentDrivingNotification.subscribe(response => {
      console.log(response);
      this.storedDrivingNotification = response;
    });
    this.drivingSubscription = this._drivingService.checkIfUserHasActiveDriving(this.currentUserId).subscribe(
      res => {
        this.activeRide = res;
        console.log(res);
        if(res){
          this.store.dispatch(new UpdateDrivingNotification(res)).subscribe()
          console.log(res);
        }
      }
    )
  }
}
