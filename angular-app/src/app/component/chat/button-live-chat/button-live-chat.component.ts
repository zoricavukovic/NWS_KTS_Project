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
  chatRoom: ChatRoom;
  numOfNotifications = 0;
  chatRoomSubscription: Subscription;
  isAdmin: boolean = false;
  loggedUserEmail: string;

  constructor(
    public authService: AuthService,
    private chatRoomService: ChatRoomService
  ) {}

  ngOnInit(): void {
    if (this.isLoggedInRegularOrDriver()) {
      this.loadChatRoom();
    }
  }

  loadChatRoom() {
    this.chatRoomSubscription = this.chatRoomService
        .getUserChatRoom(this.authService.getCurrentUser.email)
        .subscribe(res => {
          this.chatRoom = res;
          this.loggedUserEmail = this.authService.getCurrentUser.email;
          if (!this.showChatPoupup && this.chatRoom) {
            this.updateNotifications();
          }
        });
  }

  isLoggedInRegularOrDriver(): boolean {
    return this.authService.getCurrentUser !== null && !this.authService.getCurrentUser.isUserAdmin();
  }

  updateNotifications(): number {
    if (this.isLoggedInRegularOrDriver()){
      if (this.authService.getCurrentUser.email !== this.loggedUserEmail){
        this.loadChatRoom();
      }

      this.numOfNotifications =  this.chatRoomService.getNumOfNotSeenMessages(
        this.chatRoom,
        this.isAdmin
      );
    }

    return this.numOfNotifications;
  }

  ngOnDestroy(): void {
    if (this.chatRoomSubscription) {
      this.chatRoomSubscription.unsubscribe();
      this.chatRoomService.resetDataRegularAndDriver();
    }
  }
}
