import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Observable } from 'rxjs';
import { RateReview, Review } from '../model/review/rate-review';

@Injectable({
  providedIn: 'root',
})
export class ReviewService {
  constructor(private http: HttpClient, private configService: ConfigService) {}

  saveReview(reviewRequest: RateReview): Observable<RateReview> {
    return this.http.post<RateReview>(
      this.configService.rate_driver_vehicle_url,
      reviewRequest,
      { headers: this.configService.getHeader() }
    );
  }

  getReviewedDrivingsForUser(id: number): Observable<number[]> {
    return this.http.get<number[]>(
      this.configService.reviewed_drivings_url(id),
      { headers: this.configService.getHeader() }
    );
  }

  getReviewsForDriver(id: number): Observable<Review[]> {
    return this.http.get<Review[]>(
      this.configService.get_reviews_for_driver(id),
      { headers: this.configService.getHeader() }
    );
  }

  createRateReview(
    vehicleRate: number,
    driverRate: number,
    message: string,
    driving: number,
    userEmail?: string,
    id?: number
  ): RateReview {
    return {
      id: id,
      vehicleRate: vehicleRate,
      driverRate: driverRate,
      message: message,
      driving: driving,
      userEmail: userEmail,
    };
  }
}
