import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { environment } from 'src/environments/environment';
import { Router } from '@angular/router';
import {ToastrService} from "ngx-toastr";
import {ChatRoomService} from "../chat-room-service/chat-room.service";
import {VehicleService} from "../vehicle-service/vehicle.service";
import {DriverActivityResetNotification} from "../../models/notification/driver-activity-reset-notification";
import {ChatRoomWithNotify} from "../../models/message/chat-room-with-notify";
import {ChatRoom} from "../../models/message/chat-room";
import {DrivingService} from "../driving-service/driving.service";
import {VehicleCurrentLocation} from "../../models/vehicle/vehicle-current-location";
import {DrivingNotification} from "../../models/notification/driving-notification";
import {DrivingNotificationService} from "../driving-notification-service/driving-notification.service";
import {BlockNotification} from "../../models/notification/block-notification";
import {DriverService} from "../driver-service/driver.service";

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private stompClient = null;
  private globalStompClient = null;
  initialized = false;
  initializedGlobal = false;

  constructor(
    private chatRoomService: ChatRoomService,
    private vehicleService: VehicleService,
    private drivingNotificationService: DrivingNotificationService,
    private driverService: DriverService,
    private router: Router,
    private toast: ToastrService,
    private drivingService: DrivingService
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

      const that = this;

      this.stompClient.connect({}, function (frame) {
        that.vehicleUpdateCoordinate();
        that.stompClient.subscribe(
          environment.publisherUrl + localStorage.getItem('email') + '/connect',
          message => {
            if (message !== null && message !== undefined) {
              that.checkNotificationType(message.body);
            }
          }
        );
      });
    }
  }

  vehicleUpdateCoordinate() {
    this.stompClient.subscribe(environment.publisherUrl + localStorage.getItem('email') + '/update-driving', (message: { body: string }) => {
      if (
        (message !== null && message !== undefined) ||
        message?.body !== null
      ) {
        const vehicleCurrentLocation: VehicleCurrentLocation = JSON.parse(message.body);
        this.drivingService.updateRide(vehicleCurrentLocation);
      }

      //   this.toast.info('Driving is finished.Tap to see details!')
      //     .onTap.subscribe(action => this.router.navigate(['/serb-uber/user/map-page-view', drivingNotificationDetails.drivingId]));
      // });
    });
  }

  checkNotificationType(message: string) {
    if (this.isActivityResetNotification(message)) {
      this.driverService.showActivityStatusResetNotification(
        JSON.parse(message)
      );
    } else if (this.isBlockingNotification(message)) {
      this.logOutUser();

    } else {
      this.isMessageType(message)
        ? this.chatRoomService.addMessage(JSON.parse(message))
        : this.isDrivingNotification(message)
        ? this.drivingNotificationService.showNotification(JSON.parse(message))
        : this.drivingNotificationService.showDrivingStatus(
            JSON.parse(message)
          );
    }
  }

  logOutUser(): void {
    this.disconnect();
    localStorage.clear();
    this.router.navigate(['/login']);
    window.location.reload();
  }

  globalConnect() {
    if (!this.initializedGlobal) {
      this.initializedGlobal = true;
      const serverUrl = environment.webSocketUrl;
      const ws = new SockJS(serverUrl);
      this.globalStompClient = Stomp.over(ws);

      const that = this;

      this.globalStompClient.connect({}, function (frame) {
        that.globalStompClient.subscribe(
          environment.publisherUrl + 'global/connect',
          message => {
            if (
              (message !== null && message !== undefined) ||
              message?.body !== null
            ) {
              const vehicleCurrentLocation: VehicleCurrentLocation[] =
                JSON.parse(message.body);
              that.vehicleService.addVehicle(vehicleCurrentLocation);
            }
          }
        );
      });
    }
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

  private isDrivingNotification(message: string): boolean {
    try {
      const parsed: DrivingNotification = JSON.parse(message);
      return parsed.drivingNotificationType === 'LINKED_USER';
    } catch (e) {
      return false;
    }
  }

  private isMessageType(webSocketNotification: string): boolean {
    try {
      const parsed: ChatRoomWithNotify = JSON.parse(webSocketNotification);
      return parsed.notifyAdmin !== null && parsed.notifyAdmin !== undefined;
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
}
