import { Injectable } from "@angular/core";
import { ReviewRequest } from "../model/request/review-request";
import { HttpClient } from "@angular/common/http";
import { ConfigService } from "./config.service";
import { map, Observable } from 'rxjs';
import { Review } from "../model/response/review";

@Injectable({
    providedIn: 'root'
  })
  export class ReviewService {
    constructor(
        private http: HttpClient,
        private configService: ConfigService
      ) { }

  saveReview(reviewRequest: ReviewRequest) : Observable<Review> {

    return this.http.post<Review>(this.configService.rate_driver_vehicle_url, reviewRequest, {headers: this.configService.header})
  }

  getReviewedDrivingsForUser(id: number){
    return this.http.get<number[]>(this.configService.reviewed_drivings_url(id), {headers: this.configService.header});
  }
}
