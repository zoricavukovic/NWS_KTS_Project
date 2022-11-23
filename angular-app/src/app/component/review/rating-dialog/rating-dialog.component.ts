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
  id: number;
  userEmail: string;
  message = '';

  reviewSubscription: Subscription;

  constructor(
    private dialogRef: MatDialogRef<RatingDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data,
    public reviewService: ReviewService
  ) {
    this.id = data.id;
    this.userEmail = data.userEmail;
  }
  cancelBtn(): void {
    this.dialogRef.close();
  }

  confirm(): void {
    //subscribe() ne radi
    this.reviewSubscription = this.reviewService
      .saveReview(
        this.reviewService.createRateReview(
          this.ratingVehicle,
          this.ratingDriver,
          this.message,
          this.id,
          this.userEmail
        )
      )
      .subscribe(data => {
        console.log(data);
        this.dialogRef.close();
      });
  }

  ngOnDestroy(): void {
    if (this.reviewSubscription) {
      this.reviewSubscription.unsubscribe();
    }
  }
}
