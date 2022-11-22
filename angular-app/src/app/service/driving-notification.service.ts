import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { DrivingNotificationRequest } from '../model/request/driving-notification-request';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root',
})
export class DrivingNotificationService {
  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private toast: ToastrService
  ) {}

  saveDrivingNotification(
    drivingNotificationRequest: DrivingNotificationRequest
  ) {
    return this.http.post<DrivingNotificationRequest>(
      this.configService.driving_notifications_url,
      drivingNotificationRequest
    );
  }

  showNotification(drivingNotificationRequest: DrivingNotificationRequest) {
    this.toast.success('uspesnoooo' + drivingNotificationRequest.price);
  }
}
