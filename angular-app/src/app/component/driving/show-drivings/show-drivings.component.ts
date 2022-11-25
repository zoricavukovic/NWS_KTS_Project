import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/service/auth.service';
import { Driving } from 'src/app/model/driving/driving';
import { User } from 'src/app/model/user/user';
import { PageEvent } from '@angular/material/paginator';
import { DrivingService } from 'src/app/service/driving.service';
import { ReviewService } from 'src/app/service/review.service';

@Component({
  selector: 'app-show-drivings',
  templateUrl: './show-drivings.component.html',
  styleUrls: ['./show-drivings.component.css'],
})
export class ShowDrivingsComponent implements OnInit, OnDestroy {
  drivings: Driving[] = [];
  currentUser: User;
  userId: number;
  pageSize = 3;
  pageNumber = 0;
  totalPages: number;
  selectedSortBy = 'Date';
  selectedSortOrder = 'Descending';

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
  ) {}

  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get('id');
    this.currentUser = this.authService.getCurrentUser;

    this.drivingCountSubscription = this.drivingService
      .getCountDrivings(this.userId)
      .subscribe(response => {
        console.log(response);
        this.totalPages = response / this.pageSize;
      });

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
        console.log(this.drivings);
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
    this.pageSize = +pageEvent.pageSize;
    this.pageNumber = +pageEvent.pageIndex;
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
