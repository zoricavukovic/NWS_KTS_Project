import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Driver } from 'src/app/model/user/driver';
import { DriverService } from 'src/app/service/driver.service';

@Component({
  selector: 'app-user-average-rate',
  templateUrl: './user-average-rate.component.html',
  styleUrls: ['./user-average-rate.component.css'],
})
export class UserAverageRateComponent implements OnInit, OnDestroy {
  @Input() driverId: number;

  driverSubscription: Subscription;
  driver: Driver = null;

  constructor(private driverService: DriverService) {}

  ngOnInit(): void {
    this.driverSubscription = this.driverService
      .get(this.driverId)
      .subscribe(driver => {
        this.driver = driver;
      });
  }

  getAverageRate(): number {
    return this.driver ? (this.driver.rate + this.driver.vehicle.rate) / 2 : 0;
  }

  ngOnDestroy(): void {
    if (this.driverSubscription) {
      this.driverSubscription.unsubscribe();
    }
  }
}
