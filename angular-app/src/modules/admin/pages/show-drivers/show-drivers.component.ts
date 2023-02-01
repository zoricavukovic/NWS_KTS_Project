import { OnDestroy, OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Driver } from 'src/modules/shared/models/user/driver';
import { Subscription } from 'rxjs';
import { DriverService } from 'src/modules/shared/services/driver-service/driver.service';

@Component({
  selector: 'app-show-drivers',
  templateUrl: './show-drivers.component.html',
  styleUrls: ['./show-drivers.component.css'],
})
export class ShowDriversComponent implements OnInit, OnDestroy {
  drivers: Driver[];
  pageSize = 3;
  totalPages:number;
  currentPage = 0;

  driversSubscription: Subscription;

  constructor(private driverService: DriverService) {
    this.drivers = [];
  }

  ngOnInit(): void {
    this.driversSubscription = this.driverService
      .getWithPagination(this.currentPage, this.pageSize)
      .subscribe(response => {
        this.drivers = response;
        this.totalPages = response[0].pageNumber;
      });
  }

  changePage(newPage: number) {
    this.currentPage = newPage;
    this.driverService
      .getWithPagination(this.currentPage, this.pageSize)
      .subscribe((response: Driver[]) => {
        this.drivers = response;
        if(this.drivers.length > 0){
          this.totalPages = this.drivers[0].pageNumber;
        }
      });
  }

  ngOnDestroy(): void {
    if (this.driversSubscription) {
      this.driversSubscription.unsubscribe();
    }
  }
}
