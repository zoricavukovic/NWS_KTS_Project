import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/response/user/user';
import { AuthService } from 'src/app/service/auth.service';
import { ChatRoom } from 'src/app/model/response/messages/chat-room';
import { ChatRoomService } from 'src/app/service/chat-room.service';

@Component({
  selector: 'app-history-live-chat',
  templateUrl: './history-live-chat.component.html',
  styleUrls: ['./history-live-chat.component.css']
})
export class HistoryLiveChatComponent implements OnInit, OnDestroy {

  loggedUser: User;
  chatRooms: ChatRoom[];
  selectedChatRoom: ChatRoom;
  
  authSubscription: Subscription;
  chatRoomSubscription: Subscription;

  constructor(
    private authService: AuthService,
    private chatRoomService: ChatRoomService
  ) { }

  ngOnInit(): void {
    this.authSubscription = this.authService.getCurrentUser().subscribe(
      user => this.loggedUser = user
    );

    this.chatRoomSubscription = this.chatRoomService.getAllChatRooms(this.loggedUser.email).subscribe(
      res => {
        this.chatRooms = res;
        this.selectedChatRoom = this.getSelectedChatRoom();
      },
      error => console.log(error)
    )
  }

  getSelectedChatRoom(): ChatRoom {

    return (this.chatRooms.length > 0) ? this.chatRooms[0] : null;
  }

  ngOnDestroy(): void {
    if (this.authService) {
      this.authSubscription.unsubscribe();
    }

    if (this.chatRoomSubscription) {
      this.chatRoomSubscription.unsubscribe();
    }

  }

}
