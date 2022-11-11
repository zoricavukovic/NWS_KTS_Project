import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Driver } from '../model/response/user/driver';
@Injectable({
    providedIn: 'root'
  })
  export class DrivingService {
  
    constructor(
      private http: HttpClient,
      private configService: ConfigService,
    ) { }

    private userEmail;
    private drivingId;

    get getUserEmail(){
        return this.userEmail;
    }

    set setUserEmail(email: string){
        this.userEmail = email;
    }

    get getDrivingId(){
      return this.drivingId;
  }

  set setDrivingId(drivingId: number){
      this.drivingId = drivingId;
  }

  getDrivingsForUser(pageNumber: number, pageSize: number, selectedSortBy: string, selectedSortOrder: string){
      return  this.http.get(this.configService.drivings_url(this.userEmail, pageNumber, pageSize, selectedSortBy, selectedSortOrder));
  }

  getDrivingDetails(){
    return this.http.get(this.configService.driving_details_url(this.drivingId));
  }

}