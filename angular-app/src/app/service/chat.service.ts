import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import { environment } from 'src/environments/environment';
import { ChatRoomService } from './chat-room.service';
import { ChatRoom } from '../model/response/messages/chat-room';


@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private stompClient = null;
  initialized: boolean = false;

  constructor(private chatRoomService: ChatRoomService) {}

  
  connect(userEmail: string) {
    if (!this.initialized) {
      this.initialized = true;
      const serverUrl = environment.webSocketUrl;
      const ws = new SockJS(serverUrl);
      this.stompClient = Stomp.over(ws);
      const that = this;
      this.stompClient.connect({}, function(frame) {
        that.stompClient.subscribe(environment.publisherUrl + userEmail + "/connect", (message) => {
          if (message !== null && message !== undefined) {
            that.chatRoomService.addMessage(JSON.parse(message.body));
          }
        }); 
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
    this.stompClient.send('/app/send' , {}, JSON.stringify(message));
  }

  showMessage(message) {
    //this.messages.push(message);
  }

}