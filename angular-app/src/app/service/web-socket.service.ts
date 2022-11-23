import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';
import { environment } from 'src/environments/environment';
import { ChatRoomService } from './chat-room.service';
import { ChatRoom } from '../model/message/chat-room';
import { ChatRoomWithNotify } from '../model/message/chat-room-with-notify';
import { connect } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private stompClient = null;
  initialized: boolean = false;

  constructor(private chatRoomService: ChatRoomService) {
    if (!this.stompClient) {
      this.connect();
    }
  }

  connect() {
    if (!this.initialized) {
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
              that.chatRoomService.addMessage(JSON.parse(message.body));
            }
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

  sendMessage(chatRoom: ChatRoom, notifyAdmin: boolean) {
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
