import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import {ReviewService} from "../../services/review-service/review.service";
import {Review} from "../../models/review/rate-review";

@Component({
  selector: 'app-reviews-history',
  templateUrl: './reviews-history.component.html',
  styleUrls: ['./reviews-history.component.css']
})
export class ReviewsHistoryComponent implements OnInit, OnDestroy {

  @Input() userId: number;

  reviewSubscription: Subscription;
  reviews: Review[];

  constructor(
    private reviewService: ReviewService,
  ) {
    this.reviews = [];
  }

  ngOnInit(): void {
    if (this.userId) {
      this.loadDriverReviews();
    }
  }

  loadDriverReviews(): void {
    this.reviewSubscription = this.reviewService.getReviewsForDriver(this.userId).subscribe(
      reviews => {
        if (reviews !== null && reviews !== undefined) {
          this.reviews = reviews;
        }
      }
    );
  }

  ngOnDestroy(): void {
    if (this.reviewSubscription) {
      this.reviewSubscription.unsubscribe();
    }
  }

}
