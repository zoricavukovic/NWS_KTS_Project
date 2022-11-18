import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { ChatRoom } from '../model/response/messages/chat-room';
import { MessageRequest, MessageSeenRequest } from '../model/request/message/message-request';
import { Message } from '../model/response/messages/message';
import { ChatRoomWithNotify } from '../model/request/message/chat-room-with-notify';
import {ToastrService} from "ngx-toastr";
import { UserService } from './user.service';
import { User } from '../model/response/user/user';


@Injectable({
  providedIn: 'root'
})
export class ChatRoomService {

  chatRoomClient$ = new BehaviorSubject<ChatRoom>(null);
  adminChatRooms$ = new BehaviorSubject<ChatRoom[]>([]);

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private toast: ToastrService,
    private userService: UserService
    ) { }

  getUserChatRoom(email: string) : BehaviorSubject<ChatRoom> {
    this.http.get<ChatRoom>(this.configService.chat_rooms_url + "/" + email, {headers: this.configService.getHeader()}).subscribe(
      res => {
        this.chatRoomClient$.next(res);
      });

      return this.chatRoomClient$;
  }

  getAllChatRooms(email: string): BehaviorSubject<ChatRoom[]> {
    this.http.get<ChatRoom[]>(this.configService.all_chat_rooms_url + email, {headers: this.configService.getHeader()}).subscribe(
    res => {
      this.adminChatRooms$.next(res);
    });

    return this.adminChatRooms$;
  }

  addMessageToChatRoom(messageReq: MessageRequest): Observable<ChatRoom> {

    return this.http.post<ChatRoom>(this.configService.chat_rooms_url, messageReq, {headers: this.configService.getHeader()});
  }

  resolveChatRoom(id: number): Observable<ChatRoom> {

    return this.http.post<ChatRoom>(this.configService.resolve_chat_room_url, id, {headers: this.configService.getHeader()});
  }

  setMessagesAsSeen(messageSeenRequest: MessageSeenRequest): Observable<ChatRoom> {

    return this.http.post<ChatRoom>(this.configService.set_messages_as_seen, messageSeenRequest, {headers: this.configService.getHeader()});
  }

  clientMessageNotSeen(message: Message) {

    return !message.seen && !message.adminResponse;
  }

  adminMessageNotSeen(message: Message) {

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

  getNumOfNotSeenMessages(currentChatRoom: ChatRoom, adminLogged: boolean): number {
    let notificationsNum: number = 0;
    for (let mes of currentChatRoom.messages) {
      if (adminLogged && this.clientMessageNotSeen(mes)) {
        notificationsNum += 1;
      } else if (!adminLogged && this.adminMessageNotSeen(mes)) {
        notificationsNum += 1;
      }
    }

    return notificationsNum;
  }

  notifyAdmin(chatRoomWithNotify: ChatRoomWithNotify): void {
    if (chatRoomWithNotify.notifyAdmin && chatRoomWithNotify.chatRoom.admin.email === this.getCurrentUserEmail()) {
      this.toast.info("New message received!",
      `You have new message from ${chatRoomWithNotify.chatRoom.client.name + '' + chatRoomWithNotify.chatRoom.client.surname}.`);
    }
  }

  resetDataAdmin(): void {
    this.adminChatRooms$.next([]);
  }

  resetDataRegularAndDriver(): void {
    this.chatRoomClient$.next(null);
  }

  addMessage(chatRoomWithNotify: ChatRoomWithNotify): void {
    let chatRoom = chatRoomWithNotify.chatRoom;
    this.chatRoomClient$.next(chatRoom);
    let copyChatRoom: ChatRoom[] = this.adminChatRooms$.value;

    for (let i = 0; i < copyChatRoom.length; i++) {
      if ((copyChatRoom[i].id === chatRoom.id)) {
        copyChatRoom.splice(i, 1);
        break;
      }
    }

    this.notifyAdmin(chatRoomWithNotify);
    this.adminChatRooms$.next([chatRoom, ...this.adminChatRooms$.value]);
  }

}
