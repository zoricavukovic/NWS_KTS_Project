import { Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ChatRoom } from 'src/app/model/message/chat-room';
import { ToastrService } from 'ngx-toastr';
import { ChatRoomService } from 'src/app/service/chat-room.service';
import { Subscription } from 'rxjs';
import { WebSocketService } from 'src/app/service/web-socket.service';

@Component({
  selector: 'app-messages-box',
  templateUrl: './messages-box.component.html',
  styleUrls: ['./messages-box.component.scss'],
})
export class MessagesBoxComponent implements OnInit, OnDestroy {
  @Input() selectedChatRoom: ChatRoom;

  constructor(
    private toast: ToastrService,
    private chatRoomService: ChatRoomService,
    private chatService: WebSocketService
  ) {}

  isAdmin: boolean = true;
  newMessage: string = '';
  sendMessageSubscription: Subscription;
  resolveSubscription: Subscription;

  ngOnInit(): void {}

  messageIsInvalid(): boolean {
    return this.newMessage.length >= 100;
  }

  onSend(): void {
    if (!this.messageIsInvalid()) {
      this.sendMessageSubscription = this.chatRoomService
        .addMessageToChatRoom(
          this.chatRoomService.createMessageRequest(
            this.selectedChatRoom.id,
            this.newMessage,
            this.selectedChatRoom.client.email,
            this.selectedChatRoom.admin.email,
            this.isAdmin
          )
        )
        .subscribe(
          res => {
            this.newMessage = '';
            this.selectedChatRoom = res;
            this.chatService.sendMessage(res, false);
          },
          error =>
            this.toast.error(
              'Message cannot be sent!',
              'Error occured, try again later.'
            )
        );
    }
  }

  onResolve() {
    if (!this.selectedChatRoom.resolved) {
      this.resolveSubscription = this.chatRoomService
        .resolveChatRoom(this.selectedChatRoom.id)
        .subscribe(
          res => {
            this.chatService.sendMessage(res, false);
            this.selectedChatRoom = res;
            this.toast.success(
              'Chat room is resolved!',
              'Chat room is marked as resolved.'
            );
          },
          error =>
            this.toast.error(
              'Chat room cannot be resolved!',
              'You cannot resolve resolved chat rooms.'
            )
        );
    }
  }

  ngOnDestroy(): void {
    if (this.sendMessageSubscription) {
      this.sendMessageSubscription.unsubscribe();
    }

    if (this.resolveSubscription) {
      this.resolveSubscription.unsubscribe();
    }
  }
}
