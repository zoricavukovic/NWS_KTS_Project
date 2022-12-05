import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from 'src/app/model/user/user';

@Component({
  selector: 'app-driving-details-passengers',
  templateUrl: './driving-details-passengers.component.html',
  styleUrls: ['./driving-details-passengers.component.css'],
})
export class DrivingDetailsPassengersComponent implements OnInit {
  @Input() users: User[];
  @Input() base64Prefix: string;

  constructor(private router: Router) {}

  ngOnInit() {
    console.log(this.users);
  }
  goToPassengerProfile(id: number): void {
    this.router.navigate([`/user-profile/${id}`]);
  }
}
