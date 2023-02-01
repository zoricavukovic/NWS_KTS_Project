import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import {MessageRequest, MessageSeenRequest} from "../../models/message/message-request";
import {ConfigService} from "../config-service/config.service";
import {User} from "../../models/user/user";
import {ChatRoomWithNotify} from "../../models/message/chat-room-with-notify";
import {ChatRoom} from "../../models/message/chat-room";
import {MessageResponse} from "../../models/message/message-response";

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
      .get<ChatRoom>(this.configService.activeChatRoomsForUserUrl(email))
      .subscribe(res => {
        this.chatRoomClient$.next(res);
      });

    return this.chatRoomClient$;
  }

  getAllChatRooms(email: string): BehaviorSubject<ChatRoom[]> {
    this.http
      .get<ChatRoom[]>(this.configService.allChatRoomsForUserUrl(email))
      .subscribe(res => {
        this.adminChatRooms$.next(res);
      });

    return this.adminChatRooms$;
  }

  addMessageToChatRoom(messageReq: MessageRequest): Observable<ChatRoom> {
    return this.http.post<ChatRoom>(
      this.configService.CHAT_ROOMS_URL,
      messageReq
    );
  }

  resolveChatRoom(id: number): Observable<ChatRoom> {
    return this.http.post<ChatRoom>(
      this.configService.RESOLVE_CHAT_URL,
      id
    );
  }

  setMessagesAsSeen(
    messageSeenRequest: MessageSeenRequest
  ): Observable<ChatRoom> {
    return this.http.post<ChatRoom>(
      this.configService.SET_MESSAGE_SEEN_URL,
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
          `You have new message from ${chatRoomWithNotify.chatRoom.client.name} ${chatRoomWithNotify.chatRoom.client.surname}.`
        )
        .onTap.subscribe(res => {
          this.router.navigate(['/serb-uber/admin/messages']);
          console.log(res);
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
