import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Observable } from 'rxjs';
import { RateReview, Review } from '../model/review/rate-review';
import { HeadersService } from './headers.service';
import { GenericService } from './generic.service';

@Injectable({
  providedIn: 'root',
})
export class ReviewService extends GenericService<RateReview> {
  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private headersService: HeadersService
  ) {
    super(http, `${configService.api_url}/reviews`, headersService);
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
    drivingId?: number,
    userId?: number
  ): RateReview {
    return {
      vehicleRate: vehicleRate,
      driverRate: driverRate,
      message: message,
      drivingId: drivingId,
      userId: userId,
    };
  }
}
