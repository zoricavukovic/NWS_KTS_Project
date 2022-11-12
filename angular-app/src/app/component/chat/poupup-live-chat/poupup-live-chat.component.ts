import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subscription } from 'rxjs';
import { Message } from 'src/app/model/response/messages/message';
import { User } from 'src/app/model/response/user/user';
import {ToastrService} from "ngx-toastr";
import { ChatService } from 'src/app/service/chat.service';
import { ChatRoom } from 'src/app/model/response/messages/chat-room';
import { ChatRoomService } from 'src/app/service/chat-room.service';
import { MessageRequest } from 'src/app/model/request/message-request';

@Component({
  selector: 'app-poupup-live-chat',
  templateUrl: './poupup-live-chat.component.html',
  styleUrls: ['./poupup-live-chat.component.scss']
})
export class PoupupLiveChatComponent implements OnInit, OnDestroy {
  @Output() onCloseEvent = new EventEmitter();
  @Input() loggedUser: User;

  constructor(
    private chatRoomService: ChatRoomService,
    private toast: ToastrService,
    private chatService: ChatService
  ) { }
  
  chatRoom: ChatRoom;
  chatRoomSubscription: Subscription;
  sendMessageSubscription: Subscription;
  changedRole: boolean = false;
  previousMessage: Message;
  isAdmin: boolean = false;
  newMessage: string = '';
  
  ngOnInit(): void {
    this.chatRoomSubscription = this.chatRoomService.getUserChatRoom(this.loggedUser.email).subscribe(
      res => {
        this.chatRoom = res;
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

    getChatRoomIdIfExist(): number {
      if (this.chatRoom !== null && this.chatRoom !== undefined) {

        return this.chatRoom.id;
      }

      return null;
    }

    findAdminIfExist(): string {
      if (this.chatRoom !== null && this.chatRoom !== undefined) {

        return this.chatRoom.admin.email;
      }

      return null;
    }

    onSend(): void {
      if (this.validateMessage()) {
        this.sendMessageSubscription = this.chatRoomService.addMessageToChatRoom(
        new MessageRequest(
          this.getChatRoomIdIfExist(),
          this.newMessage,
          this.loggedUser.email,
          this.findAdminIfExist(),
          this.isAdmin
        )
      ).subscribe(
        res => {
          this.newMessage = '';
          this.chatService.sendMessage(res);
        },
        error => this.toast.error("Message cannot be sent!", "All our operators are currently busy! Try later.")
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

    fireOnClose() {
      this.onCloseEvent.emit(false);
    }

    ngOnDestroy(): void {
      if (this.chatRoomSubscription){
        this.chatRoomSubscription.unsubscribe();
      }

      if (this.sendMessageSubscription) {
        this.sendMessageSubscription.unsubscribe();
      }

    }

}
