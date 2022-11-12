import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { ChatRoom } from '../model/response/messages/chat-room';
import { MessageRequest } from '../model/request/message-request';


@Injectable({
  providedIn: 'root'
})
export class ChatRoomService {

  chatRoomClient$ = new BehaviorSubject<ChatRoom>(null);

  constructor( 
    private http: HttpClient,
    private configService: ConfigService
    ) { }

  getUserChatRoom(email: string) : BehaviorSubject<ChatRoom> {
    this.http.get<ChatRoom>(this.configService.chat_rooms_url + "/" + email).subscribe(
      res => {
        this.chatRoomClient$.next(res);
      });

      return this.chatRoomClient$;
  }

  addMessageToChatRoom(messageReq: MessageRequest): Observable<ChatRoom> {

    return this.http.post<ChatRoom>(this.configService.chat_rooms_url, messageReq);
  }

  addMessage(chatRoom: ChatRoom) {
    this.chatRoomClient$.next(chatRoom);
  }

}
