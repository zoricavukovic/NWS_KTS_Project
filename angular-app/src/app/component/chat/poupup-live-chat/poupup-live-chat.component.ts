import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { Subscription } from 'rxjs';
import { Message } from 'src/app/model/response/messages/message';
import { User } from 'src/app/model/response/user/user';
import {ToastrService} from "ngx-toastr";
import { ChatService } from 'src/app/service/chat.service';
import { ChatRoom } from 'src/app/model/response/messages/chat-room';
import { ChatRoomService } from 'src/app/service/chat-room.service';
import { MessageRequest } from 'src/app/model/request/message/message-request';

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
        if (this.chatRoom) {
          this.checkIfResolved();
        }
      }
    );
  }

  //ako je resolved kreiraj novu
  checkIfResolved() {
    if (this.chatRoom.resolved){
      this.toast.success("Problem solved!", "Thank you for using live chat.");
      this.chatRoomSubscription = this.chatRoomService.getUserChatRoom(this.loggedUser.email).subscribe(
      res => {
        this.chatRoom = res;
      }
    );
    }
  }

  validateMessage(): boolean {
    if (this.newMessage.length >= 100) {
      this.toast.error("Message cannot be sent!", "Message is too long!");

      return false;
    }

    return true;
  }

  checkIfChatRoomValid(): boolean {

    return (this.chatRoom !== null && this.chatRoom !== undefined && !this.chatRoom.resolved)
  }

  getChatRoomIdIfExist(): number {
    if (this.checkIfChatRoomValid()) {

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
        this.chatService.sendMessage(res, true);
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
