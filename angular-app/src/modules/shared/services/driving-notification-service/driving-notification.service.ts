import { Injectable} from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import {Router} from "@angular/router";
import {Observable} from "rxjs";
import {ConfigService} from "../config-service/config.service";
import {GenericService} from "../generic-service/generic.service";
import {DrivingNotification} from "../../models/notification/driving-notification";

@Injectable({
  providedIn: 'root',
})
export class DrivingNotificationService extends GenericService<DrivingNotification> {

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private toast: ToastrService,
    private _router: Router,
  ) {
    super(http, configService.DRIVING_NOTIFICATIONS_URL);
  }

  updateRideStatus(drivingNotificationNumber: number, accept: boolean, email: string): Observable<DrivingNotification> {

    return this._http.put<DrivingNotification>(this.configService.acceptDrivingUrl(drivingNotificationNumber, accept, email), null);
  }
}
