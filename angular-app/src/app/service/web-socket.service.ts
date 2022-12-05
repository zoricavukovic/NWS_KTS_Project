import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { environment } from 'src/environments/environment';
import { ChatRoomService } from './chat-room.service';
import { ChatRoom } from '../model/message/chat-room';
import { ChatRoomWithNotify } from '../model/message/chat-room-with-notify';
import { VehicleService } from './vehicle.service';
import { VehicleCurrentLocation } from '../model/vehicle/vehicle-current-location';
import { DrivingNotificationRequest } from '../model/request/driving-notification-request';
import { DrivingNotificationService } from './driving-notification.service';
import { DriverService } from './driver.service';
import { DriverActivityResetNotification } from '../model/notification/driver-activity-reset-notification';
import { DrivingNotification } from '../model/notification/driving-notification';
import { BlockNotification } from '../model/notification/block-notification';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private stompClient = null;
  private globalStompClient = null;
  initialized: boolean = false;
  initializedGlobal: boolean = false;

  constructor(
    private chatRoomService: ChatRoomService,
    private vehicleService: VehicleService,
    private drivingNotificationService: DrivingNotificationService,
    private driverService: DriverService,
    private router: Router
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
      return (parsed.email !== null && parsed.email !== undefined && (parsed.active !== null || true));
    } catch (e) {
      return false;
    }
  }

  private isDrivingNotification(message: string): boolean {
    try {
      const parsed: DrivingNotification = JSON.parse(message);
      return parsed.drivingNotificationType === 'LINKED_USERS';
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
    console.log('Disconnected!');
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

  sendNotification(drivingNotificationRequest: DrivingNotificationRequest) {
    if (!this.stompClient) {
      this.initialized = false;
      this.connect();
    }
    this.stompClient.send(
      '/app/send/notification',
      {},
      JSON.stringify(drivingNotificationRequest)
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
