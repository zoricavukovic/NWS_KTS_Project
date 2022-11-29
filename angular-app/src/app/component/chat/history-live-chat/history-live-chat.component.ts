import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/service/auth.service';
import { ChatRoom } from 'src/app/model/message/chat-room';
import { ChatRoomService } from 'src/app/service/chat-room.service';
import { MessageSeenRequest } from 'src/app/model/message/message-request';
import { User } from 'src/app/model/user/user';

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
  firstChatRoomIndex: number = 0;
  initialized: boolean = false;
  lastSelectedIndex: number;
  loggedUser: User = null;

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
    for (let i: number = 0; i < this.chatRooms.length; i++) {
      if (this.chatRooms[i].id === this.selectedChatRoom.id) {
        this.selectedChatRoom = this.chatRooms[i];
        this.setChatRoomMessagesToSeen(i);
      }
    }
  }

  setChatRoomMessagesToSeen(index: number) {
    this.selectedChatRoom = this.chatRooms[index];
    let changed: boolean = false;

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
