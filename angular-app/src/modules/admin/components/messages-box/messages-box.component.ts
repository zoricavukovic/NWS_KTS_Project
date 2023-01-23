import { Component, Input, OnDestroy } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import {ChatRoom} from "../../../shared/models/message/chat-room";
import {WebSocketService} from "../../../shared/services/web-socket-service/web-socket.service";
import {ChatRoomService} from "../../../shared/services/chat-room-service/chat-room.service";

@Component({
  selector: 'app-messages-box',
  templateUrl: './messages-box.component.html',
  styleUrls: ['./messages-box.component.scss'],
})
export class MessagesBoxComponent implements OnDestroy {
  @Input() selectedChatRoom: ChatRoom;

  constructor(
    private toast: ToastrService,
    private chatRoomService: ChatRoomService,
    private chatService: WebSocketService
  ) {}

  isAdmin = true;
  newMessage = '';
  sendMessageSubscription: Subscription;
  resolveSubscription: Subscription;

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
              error.error,
              'Chat room cannot be resolved!'
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
