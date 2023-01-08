import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Driver } from 'src/app/model/user/driver';

@Component({
  selector: 'app-driving-details-driver',
  templateUrl: './driving-details-driver.component.html',
  styleUrls: ['./driving-details-driver.component.css'],
})
export class DrivingDetailsDriverComponent {
  @Input() driver: Driver;
  @Input() base64Prefix: string;

  constructor(private router: Router) {}

  goToDriverProfile() {
    this.router.navigate([`/user-profile/${this.driver?.id}`]);
  }
}
