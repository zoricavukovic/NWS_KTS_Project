import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Review } from '../../models/review/rate-review';
import { ConfigService } from '../../services/config-service/config.service';

@Component({
  selector: 'app-review-row',
  templateUrl: './review-row.component.html',
  styleUrls: ['./review-row.component.scss']
})
export class ReviewRowComponent {

  @Input() review: Review;
  @Input() numOfReviews: number;

  constructor(
    private configService: ConfigService,
    private router: Router
  ) {
    this.review = null;
    this.numOfReviews = 0;
  }

  goToUserProfile(id: number): void {
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>
    this.router.navigate([`/serb-uber/user/user-profile/${id}`]));
  }

  getAverageRate(review: Review): number {

    return (review.driverRate + review.vehicleRate) / 2;
  }

  getGeneralImpression(review:Review): string {
    const averageRate = this.getAverageRate(review);
    if (averageRate >= 4.5) {

      return 'Excellent';
    } else if (averageRate >= 4.0) {

      return 'Very good';
    } else if (averageRate >= 3.0) {

      return 'Good';
    } else if (averageRate >= 2.5) {

      return 'OK';
    } else {

      return 'Bad'
    }
  }

}
