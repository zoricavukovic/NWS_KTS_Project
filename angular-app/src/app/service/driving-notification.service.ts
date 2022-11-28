import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { DrivingNotificationRequest } from '../model/request/driving-notification-request';
import { ToastrService } from 'ngx-toastr';
import {DrivingNotificationResponse} from "../model/notification/driving-notification-response";

@Injectable({
  providedIn: 'root',
})
export class DrivingNotificationService {
  private notificationTypeLinkedUser: string = "LINKED_USER";

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private toast: ToastrService
  ) {}

  saveDrivingNotification(drivingNotificationRequest: DrivingNotificationRequest) {
    return this.http.post(
      this.configService.driving_notifications_url,
      drivingNotificationRequest,
      { headers: this.configService.getHeader() }
    );
  }

  accept() {
    alert('bla');
  }

  showNotification(drivingNotificationResponse: DrivingNotificationResponse) {
    drivingNotificationResponse.drivingNotificationType === this.notificationTypeLinkedUser ?
      this.toast.info(`User ${drivingNotificationResponse.senderEmail} add you as linked passenger.Tap to accept!`)
        .onTap.subscribe(action => console.log(action)) :
      this.toast.info(`Driver ${drivingNotificationResponse.senderEmail} reject your driving. \nReason for rejecting is
      ${drivingNotificationResponse.reason}`)
        .onTap.subscribe(action => console.log(action));
  }
}
