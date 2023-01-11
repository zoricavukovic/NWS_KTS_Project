import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import {User} from "../../../models/user/user";
import {ChatRoom} from "../../../models/message/chat-room";
import {AuthService} from "../../../../auth/services/auth-service/auth.service";
import {ChatRoomService} from "../../../services/chat-room-service/chat-room.service";

@Component({
  selector: 'app-button-live-chat',
  templateUrl: './button-live-chat.component.html',
  styleUrls: ['./button-live-chat.component.css'],
})
export class ButtonLiveChatComponent implements OnInit, OnDestroy {
  showChatPoupup: boolean;
  chatRoom: ChatRoom;
  numOfNotifications = 0;
  chatRoomSubscription: Subscription;
  isAdmin: boolean;
  loggedUser: User = null;

  authSubscription: Subscription;

  constructor(
    public authService: AuthService,
    private chatRoomService: ChatRoomService
  ) {
    this.showChatPoupup = false;
    this.isAdmin = false;
  }

  ngOnInit(): void {
    this.authSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        this.loggedUser = user;
        if (this.isLoggedInRegularOrDriver()) {
          this.loadChatRoom();
        }
      }
    );
  }

  loadChatRoom() {
    this.chatRoomSubscription = this.chatRoomService
        .getUserChatRoom(this.loggedUser?.email)
        .subscribe(res => {
          this.chatRoom = res;
          if (!this.showChatPoupup && this.chatRoom) {
            this.updateNotifications();
          }
        });
  }

  isLoggedInRegularOrDriver(): boolean {
    return this.loggedUser && !(this.loggedUser?.role.name === "ROLE_ADMIN");
  }

  updateNotifications(): number {
    if (this.isLoggedInRegularOrDriver()){
      // if (this.authService.getCurrentUser.email !== this.loggedUserEmail){
      //   this.loadChatRoom();
      // }

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

    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
