import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { environment } from 'src/environments/environment';
import { ChatRoomService } from './chat-room.service';
import { ChatRoom } from '../model/response/messages/chat-room';
import { DrivingNotificationService } from './driving-notification.service';
import { DrivingNotificationRequest } from '../model/request/driving-notification-request';

@Injectable({
  providedIn: 'root',
})
export class ChatService {
  private stompClient = null;
  initialized = false;

  constructor(
    private chatRoomService: ChatRoomService,
    private drivingNotificationService: DrivingNotificationService
  ) {}

  connect(userEmail: string) {
    if (!this.initialized) {
      this.initialized = true;
      const serverUrl = environment.webSocketUrl;
      const ws = new SockJS(serverUrl);
      this.stompClient = Stomp.over(ws);
      const that = this;
      this.stompClient.connect({}, function (frame) {
        that.stompClient.subscribe(
          environment.publisherUrl + userEmail + '/connect',
          response => {
            console.log(response);
            /* if (response.command === 'MESSAGE') {
              if (response !== null && response !== undefined) {
                that.chatRoomService.addMessage(JSON.parse(response.body));
              }
            } else if (response.command === 'DRIVING_NOTIFICATION') {*/
            that.drivingNotificationService.showNotification(
              JSON.parse(response.body)
            );
          }
        );
      });
    }
  }

  disconnect(): void {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }

    this.initialized = false;
    console.log('Disconnected!');
  }

  sendMessage(message: ChatRoom) {
    this.stompClient.send('/app/send', {}, JSON.stringify(message));
  }

  sendNotification(drivingNotificationRequest: DrivingNotificationRequest) {
    console.log(drivingNotificationRequest);
    this.stompClient.send(
      '/app/send/notification',
      {},
      JSON.stringify(drivingNotificationRequest)
    );
  }

  showMessage(message) {
    //this.messages.push(message);
  }
}
