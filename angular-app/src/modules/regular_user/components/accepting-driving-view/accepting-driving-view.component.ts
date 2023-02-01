import { Component, Input } from '@angular/core';
import {DrivingLocation} from "../../../shared/models/route/driving-location";
import {Vehicle} from "../../../shared/models/vehicle/vehicle";
import {User} from "../../../shared/models/user/user";

@Component({
  selector: 'app-accepting-driving-view',
  templateUrl: './accepting-driving-view.component.html',
  styleUrls: ['./accepting-driving-view.component.css'],
})
export class AcceptingDrivingViewComponent {
  @Input() locations: DrivingLocation[];
  @Input() price: number;
  @Input() passengers: User[];
  @Input() vehicle: Vehicle;
}
