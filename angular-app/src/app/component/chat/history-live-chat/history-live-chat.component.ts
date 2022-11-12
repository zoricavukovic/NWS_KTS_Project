import { Component, OnDestroy, OnInit } from '@angular/core';
import { MessageService } from 'src/app/service/message.service';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/response/user/user';
import { AuthService } from 'src/app/service/auth.service';
import { ChatService } from 'src/app/service/chat.service';
import { ChatRoom } from 'src/app/model/response/messages/chat-room';

@Component({
  selector: 'app-history-live-chat',
  templateUrl: './history-live-chat.component.html',
  styleUrls: ['./history-live-chat.component.css']
})
export class HistoryLiveChatComponent implements OnInit, OnDestroy {

  loggedUser: User;
  messages: ChatRoom;
  
  authSubscription: Subscription;
  messageSubscription: Subscription;

  constructor(
    private authService: AuthService,
    private chatService: ChatService,
    private messageService: MessageService
  ) { }

  ngOnInit(): void {
    this.authSubscription = this.authService.getCurrentUser().subscribe(
      user => this.loggedUser = user
    );

    this.messageSubscription = this.messageService.getAllMessages().subscribe(
      res => this.messages = res,
      error => console.log(error)
    )
  }

  ngOnDestroy(): void {
    if (this.authService) {
      this.authSubscription.unsubscribe();
    }

    if (this.messageSubscription) {
      this.messageSubscription.unsubscribe();
    }

  }

}
