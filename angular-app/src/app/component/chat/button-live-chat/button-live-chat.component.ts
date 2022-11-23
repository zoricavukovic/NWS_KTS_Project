import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { ChatRoom } from 'src/app/model/message/chat-room';
import { AuthService } from 'src/app/service/auth.service';
import { ChatRoomService } from 'src/app/service/chat-room.service';

@Component({
  selector: 'app-button-live-chat',
  templateUrl: './button-live-chat.component.html',
  styleUrls: ['./button-live-chat.component.css'],
})
export class ButtonLiveChatComponent implements OnInit, OnDestroy {
  showChatPoupup: boolean = false;
  numOfNotifications: number = 0;
  chatRoom: ChatRoom;
  chatRoomSubscription: Subscription;
  isAdmin: boolean = false;

  constructor(
    public authService: AuthService,
    private chatRoomService: ChatRoomService
  ) {}

  ngOnInit(): void {
    if (this.isLoggedIn()) {
      this.chatRoomSubscription = this.chatRoomService
        .getUserChatRoom(this.authService.getCurrentUser.email)
        .subscribe(res => {
          this.chatRoom = res;
          if (!this.showChatPoupup && this.chatRoom) {
            this.updateNotifications();
          }
        });
    }
  }

  isLoggedIn(): boolean {
    return this.authService.getCurrentUser !== null;
  }

  updateNotifications(): void {
    this.numOfNotifications = this.chatRoomService.getNumOfNotSeenMessages(
      this.chatRoom,
      this.isAdmin
    );
  }

  ngOnDestroy(): void {
    if (this.chatRoomSubscription) {
      this.chatRoomSubscription.unsubscribe();
      this.chatRoomService.resetDataRegularAndDriver();
    }
  }
}
