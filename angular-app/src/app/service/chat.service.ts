import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';
import { Message } from '../model/response/messages/message';
import { MessageService } from './message.service';


@Injectable({
  providedIn: 'root'
})
export class ChatService {

  private stompClient = null;
  initialized: boolean = false;
  webSocketUrl: string = 'http://localhost:8080/ws';
  publisherUrl: string = '/user/';
  roleAdmin: string = "ROLE_ADMIN";

  constructor(private messageService: MessageService) {}

  
  connect(userRole: string, userEmail: string) {
    if (!this.initialized) {
      this.initialized = true;
      const serverUrl = this.webSocketUrl;
      const ws = new SockJS(serverUrl);
      this.stompClient = Stomp.over(ws);
      const that = this;
      this.stompClient.connect({}, function(frame) {
        that.stompClient.subscribe(that.publisherUrl + userEmail + "/messages", (message) => {
          if (message !== null && message !== undefined) {
            console.log("Uspelo" + message.body);
            that.messageService.addMessage(JSON.parse(message.body));
          }
        });
      });
    }
  }

  subscribeToLocalSocket(userEmail: string): void {
    this.stompClient.subscribe(this.publisherUrl + "/" + userEmail, (message) => {
      this.showMessage(message);
      console.log("Neki event kod specificne rute.")
    });
  }

  disconnect(): void {
    if (this.stompClient != null) {
      this.stompClient.disconnect();
    }

    this.initialized = false;
    console.log('Disconnected!');
  }

  sendMessage(message: Message) {
    this.stompClient.send('/app/send' , {}, JSON.stringify(message));
  }

  showMessage(message) {
    //this.messages.push(message);
  }

}
