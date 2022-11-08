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

    return this.http.post<Review>(this.configService.rate_driver_vehicle_url, reviewRequest)
  }

  //TODO NEMA OVE METODE NA BACK-u????
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