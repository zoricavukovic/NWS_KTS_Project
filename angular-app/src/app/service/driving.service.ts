import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import {Observable} from "rxjs";
import {Driving} from "../model/response/driving";
@Injectable({
    providedIn: 'root'
  })
  export class DrivingService {

    constructor(
      private http: HttpClient,
      private configService: ConfigService,
    ) { }

  getDrivingsForUser(id:number, pageNumber: number, pageSize: number, selectedSortBy: string, selectedSortOrder: string){

      return this.http.get(this.configService.drivings_url_with_pagination_and_sort(id, pageNumber, pageSize, selectedSortBy, selectedSortOrder),
        {headers: this.configService.getHeader()});
  }

  getDrivingDetails(id: number){

      return this.http.get(this.configService.driving_details_url(id), {headers: this.configService.getHeader()});
  }

  getDrivingsForDriver(driverId: number): Observable<Driving[]>{

      return this.http.get<Driving[]>(this.configService.now_future_drivings_url(driverId), {headers: this.configService.getHeader()});
  }

  finishDriving(drivingId: number): Observable<Driving> {
      return this.http.put<Driving>(this.configService.get_finish_driving_url(drivingId), null,{headers: this.configService.getHeader()});
  }
}
