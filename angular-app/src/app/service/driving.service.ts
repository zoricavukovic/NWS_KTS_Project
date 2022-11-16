import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
@Injectable({
    providedIn: 'root'
  })
  export class DrivingService {

    constructor(
      private http: HttpClient,
      private configService: ConfigService,
    ) { }

  getDrivingsForUser(id:number, pageNumber: number, pageSize: number, selectedSortBy: string, selectedSortOrder: string){

      return this.http.get(this.configService.drivings_url_with_pagination_and_sort(id, pageNumber, pageSize, selectedSortBy, selectedSortOrder));
  }

  getDrivingDetails(id: number){

      return this.http.get(this.configService.driving_details_url(id));
  }

  getDrivingsForDriver(driverId: number){

      return this.http.get(this.configService.now_future_drivings_url(driverId));
  }
}
