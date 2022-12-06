import { Component, Inject, OnDestroy } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Subscription } from 'rxjs';
import { ReviewService } from 'src/app/service/review.service';
import { RateReview } from 'src/app/model/review/rate-review';

@Component({
  selector: 'app-rating-dialog',
  templateUrl: './rating-dialog.component.html',
  styleUrls: ['./rating-dialog.component.css'],
})
export class RatingDialogComponent implements OnDestroy {
  ratingVehicle = 5;
  ratingDriver = 0;
  drivingId: number;
  userId: number;
  message = '';

  reviewSubscription: Subscription;

  constructor(
    private dialogRef: MatDialogRef<RatingDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data,
    public reviewService: ReviewService
  ) {
    this.drivingId = data.drivingId;
    this.userId = data.userId;
  }

  confirm(): void {
    this.reviewSubscription = this.reviewService
      .create(
        this.reviewService.createRateReview(
          this.ratingVehicle,
          this.ratingDriver,
          this.message,
          this.drivingId,
          this.userId
        )
      )
      .subscribe(data => {
        this.dialogRef.close(data);
      });
  }

  ngOnDestroy(): void {
    if (this.reviewSubscription) {
      this.reviewSubscription.unsubscribe();
    }
  }
}
