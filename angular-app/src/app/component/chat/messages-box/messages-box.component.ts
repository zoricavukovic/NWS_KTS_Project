import { Component, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { ChatRoom } from 'src/app/model/response/messages/chat-room';
import {ToastrService} from "ngx-toastr";
import { ChatRoomService } from 'src/app/service/chat-room.service';
import { Subscription } from 'rxjs';
import { ChatService } from 'src/app/service/chat.service';
import { MessageRequest } from 'src/app/model/request/message-request';


@Component({
  selector: 'app-messages-box',
  templateUrl: './messages-box.component.html',
  styleUrls: ['./messages-box.component.scss']
})
export class MessagesBoxComponent implements OnInit, OnDestroy {

  @Input() selectedChatRoom: ChatRoom;

  selectedIndex: number = 0;

  constructor(
    private toast: ToastrService,
    private chatRoomService: ChatRoomService,
    private chatService: ChatService
  ) { }

  isAdmin: boolean = true;
  newMessage: string = '';
  sendMessageSubscription: Subscription;
  resolveSubscription: Subscription;

  ngOnInit(): void {
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

  onSend(): void {
      if (this.validateMessage()) {
        this.sendMessageSubscription = this.chatRoomService.addMessageToChatRoom(
        new MessageRequest(
          this.selectedChatRoom.id,
          this.newMessage,
          this.selectedChatRoom.client.email,
          this.selectedChatRoom.admin.email,
          this.isAdmin
        )
      ).subscribe(
        res => {
          this.newMessage = '';
          this.selectedChatRoom = res;
          this.chatService.sendMessage(res);
        },
        error => this.toast.error("Message cannot be sent!", "Error occured, try again later.")
      );
      }

    }

    onResolve() {
      if (!this.selectedChatRoom.resolved) {
        this.resolveSubscription = this.chatRoomService.resolveChatRoom(this.selectedChatRoom.id)
        .subscribe(
          res => {
            this.chatService.sendMessage(res);
            this.selectedChatRoom = res;
            this.toast.success("Chat room is resolved!", "Chat room is marked as resolved.")
          },
          error => this.toast.error("Chat room cannot be resolved!", "You cannot resolve resolved chat rooms.")
        );
      }
    }

    ngOnDestroy(): void {
      if (this.sendMessageSubscription){
        this.sendMessageSubscription.unsubscribe();
      }

      if (this.resolveSubscription) {
        this.resolveSubscription.unsubscribe();
      }
    }

}
