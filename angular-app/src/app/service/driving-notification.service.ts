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
      drivingNotificationRequest,
      { headers: this.configService.getHeader() }
    );
  }

  accept() {
    alert('bla');
  }

  showNotification(drivingNotificationRequest: DrivingNotificationRequest) {
    console.log(drivingNotificationRequest);

    this.toast
      .info(
        `User ${drivingNotificationRequest.senderEmail} add you as linked passenger. 
        Tap to accept!`
      )
      .onTap.subscribe(action => console.log(action));
  }
}
