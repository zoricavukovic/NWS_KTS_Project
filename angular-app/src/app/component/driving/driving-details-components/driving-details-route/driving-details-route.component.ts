import { Component, Input } from '@angular/core';
import { DrivingLocation } from 'src/app/model/route/driving-location';

@Component({
  selector: 'app-driving-details-route',
  templateUrl: './driving-details-route.component.html',
  styleUrls: ['./driving-details-route.component.css'],
})
export class DrivingDetailsRouteComponent {
  @Input() locations: DrivingLocation[];
}
