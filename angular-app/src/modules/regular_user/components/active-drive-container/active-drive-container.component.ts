import { Component, Input, OnInit } from '@angular/core';
import { Router } from "@angular/router";
import { Subscription } from 'rxjs';
import { DrivingService } from 'src/modules/shared/services/driving-service/driving.service';
import {SimpleDrivingInfo} from "../../../shared/models/driving/simple-driving-info";
import {UpdateDrivingNotification} from "../../../shared/actions/driving-notification.action";
import {Store} from "@ngxs/store";

@Component({
  selector: 'app-active-drive-container',
  templateUrl: './active-drive-container.component.html',
  styleUrls: ['./active-drive-container.component.css']
})
export class ActiveDriveContainerComponent implements OnInit {

  @Input() currentUserId: number;
  activeRide: SimpleDrivingInfo;
  drivingSubscription: Subscription;
  constructor(private _router: Router, private _drivingService: DrivingService, private store: Store) { }

  goToDrivingDetails() {
    this._router.navigate(['/serb-uber/user/map-page-view', this.activeRide.drivingId]);
  }

  ngOnInit(){
    this.drivingSubscription = this._drivingService.checkIfUserHasActiveDriving(this.currentUserId).subscribe(
      res => {
        this.activeRide = res;
        if(res){
          this.store.dispatch(new UpdateDrivingNotification(res)).subscribe()
        }
      }
    )
  }
}
