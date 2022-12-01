import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Review } from 'src/app/model/review/rate-review';
import { ReviewService } from 'src/app/service/review.service';
import { ConfigService } from 'src/app/service/config.service';

@Component({
  selector: 'app-reviews-history',
  templateUrl: './reviews-history.component.html',
  styleUrls: ['./reviews-history.component.css']
})
export class ReviewsHistoryComponent implements OnInit, OnDestroy {

  @Input() userId: number;

  reviewSubscription: Subscription;
  reviews: Review[] = [];

  constructor(
    private reviewService: ReviewService,
    private configService: ConfigService
  ) {}

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

  getBase64Prefix(): string {

    return this.configService.base64_show_photo_prefix;
  }

  getAverageRate(review: Review): number {

    return (review.driverRate + review.vehicleRate) / 2;
  }

  getGeneralImpression(review:Review): string {
    const averageRate = this.getAverageRate(review);
    if (averageRate >= 4.5) {

      return 'Excellent';
    } else if (averageRate >= 4) {

      return 'Very good';
    } else if (averageRate >= 3) {

      return 'Good';
    } else if (averageRate >= 2) {
      
      return 'Ok';
    } else {
      
      return 'Bad'
    }
  }

  ngOnDestroy(): void {
    if (this.reviewSubscription) {
      this.reviewSubscription.unsubscribe();
    }
  }

}
