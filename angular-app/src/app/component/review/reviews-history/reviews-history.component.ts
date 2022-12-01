import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Review } from 'src/app/model/review/rate-review';
import { ReviewService } from 'src/app/service/review.service';
import {CarouselModule} from 'primeng/carousel';

@Component({
  selector: 'app-reviews-history',
  templateUrl: './reviews-history.component.html',
  styleUrls: ['./reviews-history.component.css']
})
export class ReviewsHistoryComponent implements OnInit, OnDestroy {

  @Input() userId: number;
  @Input() isDriver: boolean = false;

  reviewSubscription: Subscription;
  reviews: Review[] = [];

  constructor(private reviewService: ReviewService) {}

  ngOnInit(): void {
    if (this.isDriver) {
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
