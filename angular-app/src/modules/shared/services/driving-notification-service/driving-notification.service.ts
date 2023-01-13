import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ToastrService } from 'ngx-toastr';
import {Router} from "@angular/router";
import {Observable} from "rxjs";
import {ConfigService} from "../config-service/config.service";
import {GenericService} from "../generic-service/generic.service";
import {DrivingNotification} from "../../models/notification/driving-notification";
import {DrivingStatusNotification} from "../../models/notification/driving-status-notification";
import { Store } from '@ngxs/store';
import { UpdateMinutesStatusDrivingNotification } from '../../actions/driving-notification.action';

@Injectable({
  providedIn: 'root',
})
export class DrivingNotificationService extends GenericService<DrivingNotification> {
  private notificationTypeLinkedUser = 'LINKED_USER';

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private toast: ToastrService,
    private _router: Router,
    private store: Store
  ) {
    super(http, configService.DRIVING_NOTIFICATIONS_URL);
  }

  showNotification(drivingNotificationResponse: DrivingNotification) {
    //NECE MOCI DA IDE NA DETAILS, JER JOS TAD VOZNJA NIJE KREIRANA
    // this.toast
    //   .info(
    //     `User ${drivingNotificationResponse.senderEmail} add you as linked passenger.Tap to accept!`
    //   )
    //   .onTap.subscribe(action => {
    //     this._router.navigate(["driving", drivingNotificationResponse.drivingId]);
    //   });
  }

  showDrivingStatus(drivingStatusNotification: DrivingStatusNotification) {
    if (drivingStatusNotification.drivingStatus === 'ACCEPTED') {
      let updatedDriving = {
        minutes: drivingStatusNotification.minutes,
        drivingStatus: drivingStatusNotification.drivingStatus
      }
      this.store.dispatch(new UpdateMinutesStatusDrivingNotification(updatedDriving));
      this._router.navigate([`/serb-uber/user/map-page-view/${drivingStatusNotification.drivingId}`]);
    } else if (drivingStatusNotification.drivingStatus === 'PENDING') {
      document.getElementById('acceptDriving').innerText =
        'Sorry! Not found free driver. Please, try later.';
      document.getElementById('acceptDriving').style.display = 'inline';
    } else if (drivingStatusNotification.drivingStatus === 'REJECTED') {
      this.toast.info(
        `Driver ${drivingStatusNotification.driverEmail} reject your driving. \nReason for rejecting is
  ${drivingStatusNotification.reason}`
      );
    }
  }

  updateRideStatus(drivingId: number, accept: boolean, email: string): Observable<DrivingNotification> {

    return this._http.put<DrivingNotification>(this.configService.acceptDrivingUrl(drivingId, accept, email), null);
  }
}
