import { OnDestroy, OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Subscription } from 'rxjs';
import {Driver} from "../../../shared/models/user/driver";
import {DriverService} from "../../../shared/services/driver-service/driver.service";

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
      .getAll()
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
