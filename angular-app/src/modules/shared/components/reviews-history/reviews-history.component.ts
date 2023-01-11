import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Router } from '@angular/router';
import {ReviewService} from "../../services/review-service/review.service";
import {ConfigService} from "../../services/config-service/config.service";
import {Review} from "../../models/review/rate-review";

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
    private configService: ConfigService,
    private router: Router
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

  goToUserProfile(id: number): void {
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>
    this.router.navigate([`/serb-uber/user/user-profile/${id}`]));
  }

  getBase64Prefix(): string {

    return this.configService.BASE64_PHOTO_PREFIX;
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
