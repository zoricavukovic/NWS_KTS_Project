import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { Subscription } from 'rxjs';
import { MessageResponse } from 'src/app/model/message/message-response';
import { User } from 'src/app/model/user/user';
import { ToastrService } from 'ngx-toastr';
import { WebSocketService } from 'src/app/service/web-socket.service';
import { ChatRoom } from 'src/app/model/message/chat-room';
import { ChatRoomService } from 'src/app/service/chat-room.service';
import {
  MessageRequest,
  MessageSeenRequest,
} from 'src/app/model/message/message-request';

@Component({
  selector: 'app-poupup-live-chat',
  templateUrl: './poupup-live-chat.component.html',
  styleUrls: ['./poupup-live-chat.component.scss'],
})
export class PoupupLiveChatComponent implements OnInit, OnDestroy {
  @Output() onCloseEvent = new EventEmitter();
  @Output() onMessagesSeenEvent = new EventEmitter();
  @Input() loggedUser: User;

  constructor(
    private chatRoomService: ChatRoomService,
    private toast: ToastrService,
    private chatService: WebSocketService
  ) {}

  chatRoom: ChatRoom;
  chatRoomSubscription: Subscription;
  chatRoomSeenSubscription: Subscription;
  sendMessageSubscription: Subscription;
  changedRole: boolean = false;
  previousMessage: MessageResponse;
  isAdmin: boolean = false;
  newMessage: string = '';

  ngOnInit(): void {
    this.chatRoomSubscription = this.chatRoomService
      .getUserChatRoom(this.loggedUser.email)
      .subscribe(res => {
        this.chatRoom = res;
        if (this.chatRoom) {
          this.checkIfResolved();
          this.setMessagesAsSeen();
        }
      });
  }

  //ako je resolved kreiraj novu
  checkIfResolved() {
    if (this.chatRoom.resolved) {
      this.toast.success('Problem solved!', 'Thank you for using live chat.');
      this.chatRoomSubscription = this.chatRoomService
        .getUserChatRoom(this.loggedUser.email)
        .subscribe(res => {
          this.chatRoom = res;
        });
    }
  }

  setMessagesAsSeen() {
    let changed: boolean = false;
    for (let message of this.chatRoom.messages) {
      if (this.chatRoomService.adminMessageNotSeen(message)) {
        message.seen = true;
        changed = true;
      }
    }

    if (changed) {
      this.updateMessagesSeenOnServer();
    }
  }

  updateMessagesSeenOnServer() {
    this.chatRoomSeenSubscription = this.chatRoomService
      .setMessagesAsSeen(new MessageSeenRequest(this.chatRoom.id, this.isAdmin))
      .subscribe(
        res => {
          console.log(res);
          this.onMessagesSeenEvent.emit();
        },
        error => console.log(error)
      );
  }

  validateMessage(): boolean {
    if (this.newMessage.length >= 100) {
      this.toast.error('Message cannot be sent!', 'Message is too long!');

      return false;
    }

    return true;
  }

  checkIfChatRoomValid(): boolean {
    return (
      this.chatRoom !== null &&
      this.chatRoom !== undefined &&
      !this.chatRoom.resolved
    );
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
      this.sendMessageSubscription = this.chatRoomService
        .addMessageToChatRoom(
          this.chatRoomService.createMessageRequest(
            this.getChatRoomIdIfExist(),
            this.newMessage,
            this.loggedUser.email,
            this.findAdminIfExist(),
            this.isAdmin
          )
        )
        .subscribe(
          res => {
            this.newMessage = '';
            this.chatService.sendMessage(res, true);
          },
          error =>
            this.toast.error(
              'Message cannot be sent!',
              'All our operators are currently busy! Try later.'
            )
        );
    }
  }

  showDescTag(message: MessageResponse): boolean {
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
    if (this.chatRoomSubscription) {
      this.chatRoomSubscription.unsubscribe();
    }

    if (this.sendMessageSubscription) {
      this.sendMessageSubscription.unsubscribe();
    }

    if (this.chatRoomSeenSubscription) {
      this.chatRoomSeenSubscription.unsubscribe();
    }

    this.chatRoomService.resetDataRegularAndDriver();
  }
}
