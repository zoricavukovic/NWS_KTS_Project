import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { ChatRoom } from '../model/response/messages/chat-room';
import { MessageRequest } from '../model/request/message-request';

@Injectable({
  providedIn: 'root',
})
export class ChatRoomService {
  chatRoomClient$ = new BehaviorSubject<ChatRoom>(null);
  adminChatRooms$ = new BehaviorSubject<ChatRoom[]>([]);

  constructor(private http: HttpClient, private configService: ConfigService) {}

  getUserChatRoom(email: string): BehaviorSubject<ChatRoom> {
    this.http
      .get<ChatRoom>(this.configService.chat_rooms_url + '/' + email)
      .subscribe(res => {
        this.chatRoomClient$.next(res);
      });

    return this.chatRoomClient$;
  }

  getAllChatRooms(email: string): BehaviorSubject<ChatRoom[]> {
    this.http
      .get<ChatRoom[]>(this.configService.all_chat_rooms_url + email)
      .subscribe(res => {
        this.adminChatRooms$.next(res);
      });

    return this.adminChatRooms$;
  }

  addMessageToChatRoom(messageReq: MessageRequest): Observable<ChatRoom> {
    return this.http.post<ChatRoom>(
      this.configService.chat_rooms_url,
      messageReq
    );
  }

  resolveChatRoom(id: number): Observable<ChatRoom> {
    return this.http.post<ChatRoom>(
      this.configService.resolve_chat_room_url,
      id
    );
  }

  addMessage(chatRoom: ChatRoom): void {
    this.chatRoomClient$.next(chatRoom);
    const copyChatRoom: ChatRoom[] = this.adminChatRooms$.value;
    let found = false;

    for (let i = 0; i < copyChatRoom.length; i++) {
      if (copyChatRoom[i].id === chatRoom.id) {
        copyChatRoom[i] = chatRoom;
        found = true;
        break;
      }
    }

    found
      ? this.adminChatRooms$.next(copyChatRoom)
      : this.adminChatRooms$.next([chatRoom, ...this.adminChatRooms$.value]);
  }
}
