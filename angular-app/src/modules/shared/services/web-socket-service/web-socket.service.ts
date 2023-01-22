import { EventEmitter, Injectable, Output } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import { ToastrService } from "ngx-toastr";
import {ChatRoomService} from "../chat-room-service/chat-room.service";
import {VehicleService} from "../vehicle-service/vehicle.service";
import {DriverActivityResetNotification} from "../../models/notification/driver-activity-reset-notification";
import {ChatRoomWithNotify} from "../../models/message/chat-room-with-notify";
import {ChatRoom} from "../../models/message/chat-room";
import {DrivingService} from "../driving-service/driving.service";
import {VehicleCurrentLocation} from "../../models/vehicle/vehicle-current-location";
import {DrivingNotificationService} from "../driving-notification-service/driving-notification.service";
import {BlockNotification} from "../../models/notification/block-notification";
import {DriverService} from "../driver-service/driver.service";
import { CreateDrivingNotification } from '../../models/notification/create-driving-notification';
import {
  ClearStore,
  GetDrivingNotification,
  UpdateDrivings,
  UpdateMinutesStatusDrivingNotification,
  UpdateStatusDrivingNotification
} from "../../actions/driving-notification.action";
import {Store} from "@ngxs/store";
import {SimpleDrivingInfo} from "../../models/driving/simple-driving-info";
import {DrivingStatusNotification} from "../../models/notification/driving-status-notification";
import { Subscription } from 'rxjs';
import { BellNotification } from '../../models/notification/bell-notification';
import { BellNotificationsService } from '../bell-notifications-service/bell-notifications.service';
import { Driving } from '../../models/driving/driving';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private stompClient = null;
  private globalStompClient = null;
  initialized = false;
  initializedGlobal = false;
  @Output() aClickedEvent = new EventEmitter()

  currentDrivingSubscription: Subscription;

  constructor(
    private chatRoomService: ChatRoomService,
    private vehicleService: VehicleService,
    private drivingNotificationService: DrivingNotificationService,
    private driverService: DriverService,
    private router: Router,
    private toast: ToastrService,
    private drivingService: DrivingService,
    private store: Store,
    private bellNotificationService: BellNotificationsService
  ) {
    if (!this.stompClient) {
      this.initialized = false;
      this.connect();
    }

    if (!this.globalStompClient) {
      this.initializedGlobal = false;
      this.globalConnect();
    }
  }

  connect() {
    if (!this.initialized && localStorage.getItem('email') !== null) {

      this.initialized = true;
      const serverUrl = environment.webSocketUrl;
      const ws = new SockJS(serverUrl);
      this.stompClient = Stomp.over(ws);

      let that = this;

      this.stompClient.connect({}, function () {
        that.chatNotification();

        that.vehicleUpdateCoordinate();

        that.deleteDrivingNotification();

        that.deleteDrivingForCreatorNotification();

        that.passengerNotAcceptDriving();

        that.passengerNotAcceptDrivingCreator();

        that.rejectDrivingNotification();

        that.startDrivingNotification();

        that.finishDrivingNotification();

        that.driverNotFoundNotification();

        that.unsuccessfulPaymentNotification();

        that.successfulCreatedDrivingNotification();

        that.passengerAgreementNotification();

        that.bellNotificationsUpdate();
        that.newDrivingNotification();

        that.vehicleArriveNotification();
      });
    }
  }

  bellNotificationsUpdate(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/bell-notification',
      message => {
        const bellNotification:BellNotification = JSON.parse(message.body);
        this.bellNotificationService.addNotification(bellNotification);
      }
    );
  }

  //za ovaj treba i na klik da moze da ode
  passengerAgreementNotification(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/agreement-passenger',
      message => {
        const drivingNotificationResponse:CreateDrivingNotification = JSON.parse(message.body);
        this.toast
          .info(
            `User ${drivingNotificationResponse.senderEmail} add you as linked passenger.Tap to accept!`
          )
          .onTap.subscribe(action => {
          this.router.navigate(["serb-uber/user/driving-notification", drivingNotificationResponse.id]);
        });
      }
    );
  }

  driverNotFoundNotification(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/driver-not-found',
      message => {
        this.toast.info(message.body);
        this.store.dispatch(new ClearStore());
      }
    );
  }

  //ovo isto moze da vodi kao i ono prvo, a i ne mora
  successfulCreatedDrivingNotification(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/successful-driving',
      message => {
        const drivingStatusNotification: DrivingStatusNotification = JSON.parse(message.body);
        const updatedDriving = {
          minutes: drivingStatusNotification.minutes,
          drivingStatus: drivingStatusNotification.drivingStatus,
          drivingId: drivingStatusNotification.drivingId,
          vehicleId: drivingStatusNotification.vehicleId
        }

      this.store.dispatch(new GetDrivingNotification());
      this.store.dispatch(new UpdateMinutesStatusDrivingNotification(updatedDriving));
      this.router.navigate([`/serb-uber/user/map-page-view/${drivingStatusNotification.drivingId}`]);

      }
    );
  }

  unsuccessfulPaymentNotification(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/unsuccessful-payment',
      message => {
        this.toast.info(message.body);
        this.store.dispatch(new ClearStore());
      }
    );
  }

  startDrivingNotification(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/start-driving',
      (message: { body: string }) => {
        const drivingNotificationDetails: SimpleDrivingInfo =  JSON.parse(message.body);
        this.store.dispatch(new UpdateStatusDrivingNotification({active: true, drivingStatus: "ACCEPTED"}));
        this.toast.info('Ride started.Tap to follow ride!')
          .onTap.subscribe(action => {
          this.router.navigate(['/serb-uber/user/map-page-view', drivingNotificationDetails.drivingId])
        });
    });
  }

  finishDrivingNotification() {
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/finish-driving',
      (message: { body: string }) => {
      const drivingNotificationDetails: SimpleDrivingInfo =  JSON.parse(message.body);
      this.store.dispatch(new UpdateStatusDrivingNotification({active: false, drivingStatus: "FINISHED"})).subscribe();
      this.toast.info('Driving is finished.Tap to see details!')
        .onTap.subscribe(action => {
        this.router.navigate(['/serb-uber/user/map-page-view', drivingNotificationDetails.drivingId]);
      });
    });
  }

  //ovo treba
  rejectDrivingNotification(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/reject-driving',
      message => {
        this.toast.info(message.body);
        this.store.dispatch(new ClearStore());
      }
    );
  }

  //ovo isto treba, nakon 10 min, za regular
  deleteDrivingNotification(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/delete-driving',
      message => {
        this.toast.info(message.body);
        this.router.navigate(['/serb-uber/user/map-page-view/-1']);
      }
    );
  }

  //ovo je isto kao za putnike posl 10 min, samo za drivera
  deleteDrivingForCreatorNotification(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/delete-driving-creator',
      message => {
        this.toast.info(message.body);
        this.store.dispatch(new ClearStore());
      }
    );
  }

  //kad jedan ne prihvati, poruka da se svima odbija
  passengerNotAcceptDriving() {
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/passenger-not-accept-driving',
      message => {
        this.toast.info(message.body, 'Requesting ride failed');
        this.router.navigate(['/serb-uber/user/map-page-view/-1']);
      }
    );
  }

  passengerNotAcceptDrivingCreator() {
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/passenger-not-accept-driving-creator',
      message => {
        this.toast.info(message.body, 'Requesting ride failed');
        this.store.dispatch(new ClearStore());
      }
    );
  }

  vehicleUpdateCoordinate() {
    this.stompClient.subscribe(environment.publisherUrl + localStorage.getItem('email') + '/update-driving', (message: { body: string }) => {
      if ((message !== null && message !== undefined) || message?.body !== null) {
        console.log(message.body);
        const vehicleCurrentLocation: VehicleCurrentLocation = JSON.parse(message.body);
        this.drivingService.updateRide(vehicleCurrentLocation);
      }
    });
  }

  chatNotification(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/connect',
      message => {
        if (message !== null && message !== undefined) {
          this.checkNotificationType(message.body);
        }
      }
    );
  }

  checkNotificationType(message: string) {
    if (this.isActivityResetNotification(message)) {
      this.driverService.showActivityStatusResetNotification(
        JSON.parse(message)
      );
    } else if (this.isBlockingNotification(message)) {
      this.logOutUser();

    } else {
        this.chatRoomService.addMessage(JSON.parse(message));
    }
  }

  logOutUser(): void {
    this.disconnect();
    localStorage.clear();
    this.router.navigate(['/serb-uber/auth/login']);
    window.location.reload();
  }

  globalConnect() {
    // if (!this.initializedGlobal) {
    //   this.initializedGlobal = true;
      // const serverUrl = environment.webSocketUrl;
      // const ws = new SockJS(serverUrl);
      // this.globalStompClient = Stomp.over(ws);
      //
      // const that = this;
      //
      // this.globalStompClient.connect({}, function (frame) {
      //   that.globalStompClient.subscribe(
      //     environment.publisherUrl + 'global/connect',
      //     message => {
      //       if (
      //         (message !== null && message !== undefined) ||
      //         message?.body !== null
      //       ) {
      //         const vehicleCurrentLocation: VehicleCurrentLocation = JSON.parse(message.body);
      //         that.vehicleService.updateVehiclePosition(vehicleCurrentLocation);
      //       }
      //     }
      //   );
      // });
    // }
  }

  private isBlockingNotification(message: string): boolean {
    try {
      const parsed: BlockNotification = JSON.parse(message);
      return (
        parsed.blockConfirmed !== null && parsed.blockConfirmed !== undefined
      );
    } catch (e) {
      return false;
    }
  }

  private isActivityResetNotification(message: string): boolean {
    try {
      const parsed: DriverActivityResetNotification = JSON.parse(message);
      return (
        parsed.email !== null &&
        parsed.email !== undefined &&
        (parsed.active !== null || true)
      );
    } catch (e) {
      return false;
    }
  }


  disconnect(): void {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }

    this.initialized = false;
  }

  sendMessage(chatRoom: ChatRoom, notifyAdmin: boolean) {
    if (!this.stompClient) {
      this.initialized = false;
      this.connect();
    }
    this.stompClient.send(
      '/app/send/message',
      {},
      JSON.stringify(this.createChatRoomWithNotify(chatRoom, notifyAdmin))
    );
  }

  createChatRoomWithNotify(
    chatRoom: ChatRoom,
    notifyAdmin: boolean
  ): ChatRoomWithNotify {
    return {
      chatRoom: chatRoom,
      notifyAdmin: notifyAdmin,
    };
  }

  newDrivingNotification(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/new-driving',
      message => {
        const drivingStatusNotification: DrivingStatusNotification = JSON.parse(message.body);
        this.drivingService.get(drivingStatusNotification.drivingId).subscribe((response: Driving) => {
          this.toast.info("You have new ride. Tap to see details.", "New ride.").onTap.subscribe(action => {
            this.router.navigate(['/serb-uber/user/map-page-view', drivingStatusNotification.drivingId]);
          });
          this.store.dispatch(new UpdateDrivings(response));
        })
      });
  }

  vehicleArriveNotification(){
    this.stompClient.subscribe(
      environment.publisherUrl + localStorage.getItem('email') + '/vehicle-arrive',
      message => {
        const drivingStatusNotification: DrivingStatusNotification = JSON.parse(message.body);
        if(this.router.url.includes("/map-page-view/-1")){
          this.toast.info(`${message}`, "Vehicle arrive");
        }
        else{
          this.toast.info(`${message} Tap to redirect to home page and follow your ride.`, "Vehicle arrive");
        }
      });
  }
}
