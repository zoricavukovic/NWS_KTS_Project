import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Message } from 'src/app/model/response/messages/message';
import { User } from 'src/app/model/response/user/user';
import { MessageService } from 'src/app/service/message.service';
import {ToastrService} from "ngx-toastr";
import { MessageRequest } from 'src/app/model/request/message-request';
import { ChatService } from 'src/app/service/chat.service';

@Component({
  selector: 'app-poupup-live-chat',
  templateUrl: './poupup-live-chat.component.html',
  styleUrls: ['./poupup-live-chat.component.scss']
})
export class PoupupLiveChatComponent implements OnInit, OnDestroy {

  @Input() loggedUser: User;

  constructor(
    private messageService: MessageService,
    private toast: ToastrService,
    private chatService: ChatService
  ) { }
  
  messages: Message[];
  messagesSubscription: Subscription;
  sendMessageSubscription: Subscription;
  changedRole: boolean = false;
  previousMessage: Message;
  isAdmin: boolean = false;
  newMessage: string = '';
  
  ngOnInit(): void {
    this.messagesSubscription = this.messageService.getMessagesPerUser(this.loggedUser.email).subscribe(
      res => {
        this.messages = res;
      }
    );
    }

    validateMessage(): boolean {
      if (this.newMessage.length < 5) {
        this.toast.error("Message cannot be sent!", "Message must contain at least 5 characters");

        return false;
      } else if (this.newMessage.length >= 50) {
        this.toast.error("Message cannot be sent!", "Message cannot contain more than 50 characters");

        return false;
      }

      return true;
    }

    findAdminEmail(): string {
      for (let i = 0; i < this.messages.length; i++){
        if (this.messages[i].adminResponse) {
          return this.messages[i].receiver.email;
        }
      }

      return null;
    }

    onSend(): void {
      if (this.validateMessage()) {
        this.sendMessageSubscription = this.messageService.sendMessage(
        new MessageRequest(
          this.newMessage,
          this.loggedUser.email,
          this.findAdminEmail(),
          this.isAdmin
        )
      ).subscribe(
        res => {
          this.newMessage = '';
          this.chatService.sendMessage(res);
        },
        error => this.toast.error(error.error, "Message cannot be sent!")
      );
      }

    }

    showDescTag(message: Message): boolean {
      if (this.previousMessage) {
        if (!this.previousMessage.adminResponse && message.adminResponse) {
          this.changedRole = true;
        } else {
          this.changedRole = false;
        }
      } else if (message.adminResponse) {
        this.changedRole = true;
      }
      this.previousMessage = message;

      return this.changedRole;
    }

    ngOnDestroy(): void {
      if (this.messagesSubscription){
        this.messagesSubscription.unsubscribe();
      }

      if (this.sendMessageSubscription) {
        this.sendMessageSubscription.unsubscribe();
      }

    }

}
