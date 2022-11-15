import { OnDestroy, OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Driver } from 'src/app/model/response/user/driver';
import { Subscription } from 'rxjs';
import { DriverService } from 'src/app/service/driver.service';

@Component({
  selector: 'app-show-drivers',
  templateUrl: './show-drivers.component.html',
  styleUrls: ['./show-drivers.component.css'],
})
export class ShowDriversComponent implements OnInit, OnDestroy {
  drivers: Driver[];

  driversSubscription: Subscription;

  constructor(private driverService: DriverService) {}

  ngOnInit(): void {
    this.driversSubscription = this.driverService
      .getAllDrivers()
      .subscribe(response => {
        this.drivers = response;
      });
  }

  ngOnDestroy(): void {
    if (this.driversSubscription) {
      this.driversSubscription.unsubscribe();
    }
  }
}
