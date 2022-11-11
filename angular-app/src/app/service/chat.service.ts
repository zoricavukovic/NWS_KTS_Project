import { Injectable } from '@angular/core';
import * as SockJS from 'sockjs-client';
import {Stomp} from '@stomp/stompjs';


@Injectable({
  providedIn: 'root'
})
export class ChatService {

  messages: string[];
  newMessage = {
    "message": "ovo je poruka",
    "senderEmail": "ana@gmail.com",
    "receiverEmail": "pera@gmail.com",
    "adminResponse": false
  };

  private stompClient = null;

  constructor() {
    this.connect();
  }

  //dobijam konkretnog ulogovanog, ako je user onda slusa samo na svom
  //ako je admin, slusa na /user
  connect() {
    const serverUrl = 'http://localhost:8080/ws';
    const ws = new SockJS(serverUrl);
    this.stompClient = Stomp.over(ws);
    const that = this;
    this.stompClient.connect({}, function(frame) {
      that.stompClient.subscribe('/socket-publisher', (message) => {
        if (message !== null && message !== undefined) {
          console.log("Uspelo" +message.body);
          //that.messages.push(message);
        }
      });
    });
  }

  sendMessage() {
    this.stompClient.send('/socket-subscriber/send/message' , {}, JSON.stringify(this.newMessage));
  }

  showMessage(message) {
    this.messages.push(message);
  }

}
