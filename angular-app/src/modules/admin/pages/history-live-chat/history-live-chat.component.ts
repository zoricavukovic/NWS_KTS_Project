import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import {ChatRoom} from "../../../shared/models/message/chat-room";
import {MessageSeenRequest} from "../../../shared/models/message/message-request";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {ChatRoomService} from "../../../shared/services/chat-room-service/chat-room.service";


@Component({
  selector: 'app-history-live-chat',
  templateUrl: './history-live-chat.component.html',
  styleUrls: ['./history-live-chat.component.css'],
})
export class HistoryLiveChatComponent implements OnInit, OnDestroy {
  chatRooms: ChatRoom[];
  selectedChatRoom: ChatRoom;

  authSubscription: Subscription;
  chatRoomSubscription: Subscription;
  firstChatRoomIndex = 0;
  initialized = false;
  loggedUser = null;

  constructor(
    private authService: AuthService,
    private chatRoomService: ChatRoomService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        this.loggedUser = user;
      }
    );

    this.chatRoomSubscription = this.chatRoomService
      .getAllChatRooms(this.loggedUser?.email)
      .subscribe(
        res => {
          this.chatRooms = res;
          if (this.initialized) {
            this.findUpdatedSelected();
          } else {
            this.updateFirstDuringInitialization();
          }
        },
        error => console.log(error)
      );
  }

  updateFirstDuringInitialization(): void {
    this.selectedChatRoom = this.getSelectedChatRoom();
    if (this.selectedChatRoom) {
      this.setChatRoomMessagesToSeen(this.firstChatRoomIndex);
      this.initialized = true;
    }
  }

  findUpdatedSelected(): void {
    for (let i = 0; i < this.chatRooms.length; i++) {
      if (this.chatRooms[i].id === this.selectedChatRoom.id) {
        this.selectedChatRoom = this.chatRooms[i];
        this.setChatRoomMessagesToSeen(i);
      }
    }
  }

  setChatRoomMessagesToSeen(index: number) {
    this.selectedChatRoom = this.chatRooms[index];
    let changed = false;

    for (let message of this.chatRooms[index].messages) {
      if (this.chatRoomService.clientMessageNotSeen(message)) {
        message.seen = true;
        changed = true;
      }
    }

    if (changed) {
      this.updateMessagesSeenOnServer();
    }
  }

  updateMessagesSeenOnServer() {
    this.chatRoomSubscription = this.chatRoomService
      .setMessagesAsSeen(new MessageSeenRequest(this.selectedChatRoom.id, true))
      .subscribe(
        res => console.log(res),
        error => console.log(error)
      );
  }

  getSelectedChatRoom(): ChatRoom {
    return this.chatRooms.length > 0 ? this.chatRooms[0] : null;
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
    if (this.chatRoomSubscription) {
      this.chatRoomSubscription.unsubscribe();
    }

    this.initialized = false;
    this.chatRoomService.resetDataAdmin();
  }
}
