import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { PageEvent } from '@angular/material/paginator';
import {Driving} from "../../models/driving/driving";
import {User} from "../../models/user/user";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {DrivingService} from "../../services/driving-service/driving.service";
import {ReviewService} from "../../services/review-service/review.service";

@Component({
  selector: 'app-show-drivings',
  templateUrl: './show-drivings.component.html',
  styleUrls: ['./show-drivings.component.css'],
})
export class ShowDrivingsComponent implements OnInit, OnDestroy {
  drivings: Driving[] = [];
  currentUser: User = null;
  userId: number;
  pageSize: number;
  pageIndex: number;
  pageSizeDefault: number;
  pageNumber: number;
  totalPages: number;
  selectedSortBy: string;
  selectedSortOrder: string;

  sortOrder = [
    { name: 'Descending', checked: true },
    { name: 'Ascending', checked: false },
  ];

  sortBy = [
    { name: 'Date', checked: true },
    { name: 'Departure', checked: false },
    { name: 'Destination', checked: false },
    { name: 'Price', checked: false },
  ];

  currentUserSubscription: Subscription;
  drivingsSubscription: Subscription;
  reviewSubscription: Subscription;
  reviewedDrivingsSubscription: Subscription;
  drivingCountSubscription: Subscription;

  constructor(
    private authService: AuthService,
    private drivingService: DrivingService,
    private reviewService: ReviewService,
    private route: ActivatedRoute
  ) {
    this.pageSize = 3;
    this.pageIndex = 0;
    this.pageSizeDefault = 3;
    this.pageNumber = 0;
    this.totalPages = 0;
    this.selectedSortBy = 'Date';
    this.selectedSortOrder = 'Descending';
  }

  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get('id');
    this.currentUserSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        this.currentUser = user;
      }
    );

    /*this.drivingCountSubscription = this.drivingService
      .getCountDrivings(this.userId)
      .subscribe(response => {
        console.log(response);
        this.totalPages = response / this.pageSize;
      });*/

    this.drivingsSubscription = this.drivingService
      .getDrivingsForUser(
        this.userId,
        this.pageNumber,
        this.pageSize,
        this.selectedSortBy,
        this.selectedSortOrder
      )
      .subscribe((response: Driving[]) => {
        this.drivings = response;
        this.totalPages = this.drivings.at(0).pageNumber;
        this.reviewedDrivingsSubscription = this.reviewService
          .getReviewedDrivingsForUser(this.userId)
          .subscribe((response: number[]) => {
            for (const driving of this.drivings) {
              if (response.includes(driving.id)) {
                driving.hasReviewForUser = true;
              }
            }
          });
      });
  }

  selectSortOrder(name: string) {
    this.selectedSortOrder = name;
    this.drivingService
      .getDrivingsForUser(
        this.userId,
        this.pageNumber,
        this.pageSize,
        this.selectedSortBy,
        this.selectedSortOrder
      )
      .subscribe((response: Driving[]) => {
        this.drivings = response;
      });
  }

  selectSort(name: string) {
    this.selectedSortBy = name;
    this.drivingService
      .getDrivingsForUser(
        this.userId,
        this.pageNumber,
        this.pageSize,
        this.selectedSortBy,
        this.selectedSortOrder
      )
      .subscribe((response: Driving[]) => {
        this.drivings = response;
      });
  }

  onPaginate(pageEvent: PageEvent) {
    this.pageNumber = +pageEvent.pageIndex;
    this.pageIndex = +pageEvent.pageIndex;
    if (pageEvent.previousPageIndex < this.pageNumber) {
      if ((this.pageNumber + 1) * this.pageSize > this.totalPages) {
        this.pageSize = this.totalPages % this.pageSize;
      }
    } else {
      this.pageSize = this.pageSizeDefault;
    }
    this.drivingService
      .getDrivingsForUser(
        this.userId,
        this.pageNumber,
        this.pageSize,
        this.selectedSortBy,
        this.selectedSortOrder
      )
      .subscribe((response: Driving[]) => {
        this.drivings = response;
      });
  }

  ngOnDestroy(): void {
    if (this.currentUserSubscription) {
      this.currentUserSubscription.unsubscribe();
    }
    if (this.reviewSubscription) {
      this.reviewSubscription.unsubscribe();
    }
    if (this.drivingsSubscription) {
      this.drivingsSubscription.unsubscribe();
    }
    if (this.reviewedDrivingsSubscription) {
      this.reviewedDrivingsSubscription.unsubscribe();
    }
  }
}
