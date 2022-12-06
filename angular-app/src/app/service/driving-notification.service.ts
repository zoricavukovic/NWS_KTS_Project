import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { ToastrService } from 'ngx-toastr';
import { DrivingNotification } from '../model/notification/driving-notification';
import { DrivingStatusNotification } from '../model/notification/driving-status-notification';
import { GenericService } from './generic.service';
import { HeadersService } from './headers.service';

@Injectable({
  providedIn: 'root',
})
export class DrivingNotificationService extends GenericService<DrivingNotification> {
  private notificationTypeLinkedUser = 'LINKED_USER';

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private headersService: HeadersService,
    private toast: ToastrService
  ) {
    super(
      http,
      `${configService.api_url}/driving-notifications`,
      headersService
    );
  }

  showNotification(drivingNotificationResponse: DrivingNotification) {
    this.toast
      .info(
        `User ${drivingNotificationResponse.senderEmail} add you as linked passenger.Tap to accept!`
      )
      .onTap.subscribe(action => {
        console.log('blaaa');
        this.updatePerPath(drivingNotificationResponse.id).subscribe(bla => {
          console.log(bla);
        });
      });
  }

  showDrivingStatus(drivingStatusNotification: DrivingStatusNotification) {
    if (drivingStatusNotification.drivingStatus === 'ACCEPTED') {
      document.getElementById('minutes').innerText =
        drivingStatusNotification.minutes.toString();
      //document.getElementById('acceptDriving').style.visibility = 'visible';
      document.getElementById('acceptDriving').style.display = 'inline';
    } else if (drivingStatusNotification.drivingStatus === 'PENDING') {
      document.getElementById('minutes').innerText =
        'Nije pronadjen slobodan vozac';
      //document.getElementById('acceptDriving').style.visibility = 'visible';
      document.getElementById('acceptDriving').style.display = 'inline';
    } else if (drivingStatusNotification.drivingStatus === 'REJECTED') {
      this.toast.info(
        `Driver ${drivingStatusNotification.driverEmail} reject your driving. \nReason for rejecting is
  ${drivingStatusNotification.reason}`
      );
    }
  }
}
