import { Injectable } from "@angular/core";
import { ReviewRequest } from "../model/review-request";
import { HttpClient } from "@angular/common/http";
import { ConfigService } from "./config.service";
import { map } from 'rxjs';

@Injectable({
    providedIn: 'root'
  })
  export class ReviewService {
    constructor(
        private http: HttpClient,
        private configService: ConfigService
      ) { }

  saveReview(reviewRequest: ReviewRequest){
    console.log(reviewRequest);

    return this.http.post<ReviewRequest>(this.configService.rate_driver_vehicle_url, reviewRequest)
    .pipe(
      map((response) => { 
        console.log(response);
          return response;
        }
      )
    );
  }

  haveReview(drivingId: number){
    this.http.get(this.configService.have_driving_rate_url + drivingId)
    .pipe(
      map((response) => {
        console.log(response);
        return response;
      })
    );
  }
}