import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Observable } from 'rxjs';
import { Driving } from '../model/driving/driving';
import { GenericService } from './generic.service';
import {SimpleDrivingInfo} from "../model/driving/simple-driving-info";

@Injectable({
  providedIn: 'root',
})
export class DrivingService extends GenericService<Driving> {

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    super(http, `${configService.api_url}/drivings`);
  }

  getDrivingsForUser(
    id: number,
    pageNumber: number,
    pageSize: number,
    selectedSortBy: string,
    selectedSortOrder: string
  ) {
    console.log(this.configService.drivings_url_with_pagination_and_sort(
      id,
      pageNumber,
      pageSize,
      selectedSortBy,
      selectedSortOrder
    ));
    return this.http.get(
      this.configService.drivings_url_with_pagination_and_sort(
        id,
        pageNumber,
        pageSize,
        selectedSortBy,
        selectedSortOrder
      )
    );
  }

  getDrivingsForDriver(driverId: number): Observable<Driving[]> {

    return this.http.get<Driving[]>(this.configService.now_future_drivings_url(driverId));
  }

  getCountDrivings(id: number) {
    return this.http.get<number>(this.configService.get_count_drivings(id));
  }

  rejectDriving(drivingId: number, reason: string): Observable<Driving> {

    return this.http.put<Driving>(
      this.configService.reject_driving_url(drivingId),
      reason
    );
  }

  finishDriving(drivingId: number): Observable<Driving> {

    return this.http.put<Driving>(this.configService.finish_driving_url(drivingId), null);
  }

  startDriving(drivingId: number):Observable<Driving> {

    return this.http.put<Driving>(this.configService.start_driving_url(drivingId), null);
  }

  checkIfUserHasActiveDriving(id: number): Observable<SimpleDrivingInfo> {

    return this.http.get<SimpleDrivingInfo>(this.configService.check_user_has_active_driving_url(id));
  }
}
