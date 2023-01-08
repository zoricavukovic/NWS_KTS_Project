import { OnDestroy, OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { Driver } from 'src/app/model/user/driver';
import { Subscription } from 'rxjs';
import { DriverService } from 'src/app/service/driver.service';

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

  constructor(private driverService: DriverService) {}

  ngOnInit(): void {
    this.driversSubscription = this.driverService
      .getWithPagination(this.currentPage, this.pageSize)
      .subscribe(response => {
        this.drivers = response;
        this.totalPages = response[0].pageNumber;
      });
  }

  changePage(newPage: number) {
    this.currentPage = newPage - 1;
    console.log("trenutno" + this.currentPage);
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
