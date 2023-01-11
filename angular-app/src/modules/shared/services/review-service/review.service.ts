import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {GenericService} from "../generic-service/generic.service";
import {ConfigService} from "../config-service/config.service";
import {RateReview, Review} from "../../models/review/rate-review";

@Injectable({
  providedIn: 'root',
})
export class ReviewService extends GenericService<RateReview> {
  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    super(http, configService.REVIEWS_URL);
  }

  getReviewedDrivingsForUser(id: number): Observable<number[]> {

    return this.http.get<number[]>(this.configService.reviewedDrivingsForUser(id));
  }

  getReviewsForDriver(id: number): Observable<Review[]> {

    return this.http.get<Review[]>(this.configService.reviewsForDriverUrl(id));
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
