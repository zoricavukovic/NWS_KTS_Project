import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import {User} from "../../../models/user/user";

@Component({
  selector: 'app-driving-details-passengers',
  templateUrl: './driving-details-passengers.component.html',
  styleUrls: ['./driving-details-passengers.component.css'],
})
export class DrivingDetailsPassengersComponent {
  @Input() users: User[];
  @Input() base64Prefix: string;

  constructor(private router: Router) {}

  goToPassengerProfile(id: number): void {
    this.router.navigate([`/serb-uber/user/user-profile/${id}`]);
  }
}
