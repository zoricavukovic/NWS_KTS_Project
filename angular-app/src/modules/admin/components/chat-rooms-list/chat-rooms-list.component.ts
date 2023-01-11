import {
  Component,
  EventEmitter,
  Input,
  Output,
} from '@angular/core';
import {ChatRoom} from "../../../shared/models/message/chat-room";
import {ChatRoomService} from "../../../shared/services/chat-room-service/chat-room.service";

@Component({
  selector: 'app-chat-rooms-list',
  templateUrl: './chat-rooms-list.component.html',
  styleUrls: ['./chat-rooms-list.component.scss'],
})
export class ChatRoomsListComponent {
  @Output() seenMessagesEvent = new EventEmitter();

  @Input() chatRooms: ChatRoom[];

  @Input() selectedChatRoom: ChatRoom;

  isAdmin = true;

  constructor(private chatRoomService: ChatRoomService) {}

  changedSelected(index: number): void {
    this.setMessagesToSeen(index);
  }

  setMessagesToSeen(index: number): void {
    this.seenMessagesEvent.emit(index);
  }

  showNotificationBadge(currentChatRoom: ChatRoom) {
    return (
      this.getNumOfNotSeenMessages(currentChatRoom) > 0 &&
      !currentChatRoom.resolved &&
      currentChatRoom.id !== this.selectedChatRoom.id
    );
  }

  getNumOfNotSeenMessages(currentChatRoom: ChatRoom): number {
    return this.chatRoomService.getNumOfNotSeenMessages(
      currentChatRoom,
      this.isAdmin
    );
  }
}
