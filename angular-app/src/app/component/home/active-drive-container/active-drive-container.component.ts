import { Component, Input } from '@angular/core';
import { Router } from "@angular/router";
import { SimpleDrivingInfo } from "../../../model/driving/simple-driving-info";

@Component({
  selector: 'app-active-drive-container',
  templateUrl: './active-drive-container.component.html',
  styleUrls: ['./active-drive-container.component.css']
})
export class ActiveDriveContainerComponent {

  @Input() activeRide: SimpleDrivingInfo;
  constructor(private _router: Router) { }

  goToDrivingDetails() {
    this._router.navigate(['map-view', this.activeRide.drivingId]);
  }
}
