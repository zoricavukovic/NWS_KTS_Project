import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { ToastrService } from 'ngx-toastr';
import { DrivingNotification } from '../model/notification/driving-notification';
import { DrivingStatusNotification } from '../model/notification/driving-status-notification';

@Injectable({
  providedIn: 'root',
})
export class DrivingNotificationService {
  private notificationTypeLinkedUser = 'LINKED_USER';

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private toast: ToastrService
  ) {}

  saveDrivingNotification(drivingNotificationRequest: DrivingNotification) {
    console.log(drivingNotificationRequest);
    return this.http.post(
      this.configService.driving_notifications_url,
      drivingNotificationRequest,
      { headers: this.configService.getHeader() }
    );
  }

  accept() {
    alert('bla');
  }

  showNotification(drivingNotificationResponse: DrivingNotification) {
    console.log(drivingNotificationResponse);
    drivingNotificationResponse.drivingNotificationType ===
    this.notificationTypeLinkedUser
      ? this.toast
          .info(
            `User ${drivingNotificationResponse.senderEmail} add you as linked passenger.Tap to accept!`
          )
          .onTap.subscribe(action => {
            console.log('blaaa');
            this.acceptDriving(drivingNotificationResponse.id).subscribe(
              bla => {
                console.log(bla);
              }
            );
          })
      : this.toast
          .info(
            `Driver ${drivingNotificationResponse.senderEmail} reject your driving. \nReason for rejecting is
      ${drivingNotificationResponse.reason}`
          )
          .onTap.subscribe(action => console.log(action));
  }

  showDrivingStatus(drivingStatusNotification: DrivingStatusNotification) {
    const divNotification = document.getElementById('notification');
    divNotification.innerText = 'USPESNO';
    console.log('notifiiii');
  }

  acceptDriving(id: number) {
    console.log(id);
    console.log(this.configService.get_accept_driving_url(id));
    return this.http.get<DrivingNotification>(
      this.configService.get_accept_driving_url(id),
      {
        headers: this.configService.getHeader(),
      }
    );
  }
}
