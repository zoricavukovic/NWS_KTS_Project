import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { ToastrService } from 'ngx-toastr';
import { DrivingNotification } from '../model/notification/driving-notification';
import { DrivingStatusNotification } from '../model/notification/driving-status-notification';
import { GenericService } from './generic.service';
import {Router} from "@angular/router";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root',
})
export class DrivingNotificationService extends GenericService<DrivingNotification> {
  private notificationTypeLinkedUser = 'LINKED_USER';

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private toast: ToastrService,
    private _router: Router
  ) {
    super(http, `${configService.api_url}/driving-notifications`);
  }

  showNotification(drivingNotificationResponse: DrivingNotification) {
    this.toast
      .info(
        `User ${drivingNotificationResponse.senderEmail} add you as linked passenger.Tap to accept!`
      )
      .onTap.subscribe(action => {
        console.log('blaaa');
        this._router.navigate(["driving", drivingNotificationResponse.id]);
      });
  }

  showDrivingStatus(drivingStatusNotification: DrivingStatusNotification) {
    if (drivingStatusNotification.drivingStatus === 'ACCEPTED') {
      console.log(drivingStatusNotification);
      this._router.navigate([`/map-view/${drivingStatusNotification.id}`]);
      // document.getElementById('spinner-overlap-div').style.visibility = 'hidden';
      document.getElementById('minutes').innerText =
        drivingStatusNotification.minutes.toString() + "min";
        setTimeout(() => 
        {
          document.getElementById('minutes').innerText =
          drivingStatusNotification.minutes.toString() + "min";
        },
        5000);
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

  updateRideStatus(drivingId: number, accept: boolean, email: string): Observable<DrivingNotification> {

    return this._http.put<DrivingNotification>(`${this.configService.api_url}/driving-notifications/update-status/${drivingId}/${accept}/${email}`, null);
  }
}
