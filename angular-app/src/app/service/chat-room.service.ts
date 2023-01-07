import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { ChatRoom } from '../model/message/chat-room';
import {
  MessageRequest,
  MessageSeenRequest,
} from '../model/message/message-request';
import { ChatRoomWithNotify } from '../model/message/chat-room-with-notify';
import { ToastrService } from 'ngx-toastr';
import { User } from '../model/user/user';
import { MessageResponse } from '../model/message/message-response';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class ChatRoomService {
  chatRoomClient$: BehaviorSubject<ChatRoom>;
  adminChatRooms$: BehaviorSubject<ChatRoom[]>;

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private toast: ToastrService,
    private router: Router
  ) {
    this.chatRoomClient$ = new BehaviorSubject<ChatRoom>(null);
    this.adminChatRooms$ = new BehaviorSubject<ChatRoom[]>([]);
  }

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

  setMessagesAsSeen(
    messageSeenRequest: MessageSeenRequest
  ): Observable<ChatRoom> {
    return this.http.post<ChatRoom>(
      this.configService.set_messages_as_seen,
      messageSeenRequest
    );
  }

  clientMessageNotSeen(message: MessageResponse) {
    return !message.seen && !message.adminResponse;
  }

  adminMessageNotSeen(message: MessageResponse) {
    return !message.seen && message.adminResponse;
  }

  getCurrentUserEmail(): string {
    const user = localStorage.getItem('user');
    let parsedUser: User;
    if (user) {
      parsedUser = JSON.parse(user);
    }

    return parsedUser ? parsedUser.email : null;
  }

  getNumOfNotSeenMessages(
    currentChatRoom: ChatRoom,
    adminLogged: boolean
  ): number {
    let notificationsNum = 0;
    if (currentChatRoom) {
      for (const mes of currentChatRoom.messages) {
        if (adminLogged && this.clientMessageNotSeen(mes)) {
          notificationsNum += 1;
        } else if (!adminLogged && this.adminMessageNotSeen(mes)) {
          notificationsNum += 1;
        }
      }
    }

    return notificationsNum;
  }

  notifyAdmin(chatRoomWithNotify: ChatRoomWithNotify): void {
    if (
      chatRoomWithNotify.notifyAdmin &&
      chatRoomWithNotify.chatRoom.admin.email === this.getCurrentUserEmail()
    ) {
      this.toast
        .info(
          'New message received!',
          `You have new message from ${
            chatRoomWithNotify.chatRoom.client.name +
            '' +
            chatRoomWithNotify.chatRoom.client.surname
          }.`
        )
        .onTap.subscribe(res => {
          this.router.navigate(['/messages']);
        });
    }
  }

  resetDataAdmin(): void {
    this.adminChatRooms$.next([]);
  }

  resetDataRegularAndDriver(): void {
    this.chatRoomClient$.next(null);
  }

  addMessage(chatRoomWithNotify: ChatRoomWithNotify): void {
    const chatRoom = chatRoomWithNotify.chatRoom;
    this.chatRoomClient$.next(chatRoom);

    const copyChatRoom: ChatRoom[] = this.adminChatRooms$.value;

    for (let i = 0; i < copyChatRoom.length; i++) {
      if (copyChatRoom[i].id === chatRoom.id) {
        copyChatRoom.splice(i, 1);
        break;
      }
    }

    this.notifyAdmin(chatRoomWithNotify);
    this.adminChatRooms$.next([chatRoom, ...this.adminChatRooms$.value]);
  }

  createMessageRequest(
    chatId: number,
    message: string,
    senderEmail: string,
    receiverEmail: string,
    adminResponse: boolean
  ): MessageRequest {
    return {
      chatId: chatId,
      message: message,
      senderEmail: senderEmail,
      receiverEmail: receiverEmail,
      adminResponse: adminResponse,
    };
  }
}
