import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { ChatRoom } from 'src/app/model/message/chat-room';
import { ChatRoomService } from 'src/app/service/chat-room.service';

@Component({
  selector: 'app-chat-rooms-list',
  templateUrl: './chat-rooms-list.component.html',
  styleUrls: ['./chat-rooms-list.component.scss'],
})
export class ChatRoomsListComponent implements OnInit, OnDestroy {
  @Output() seenMessagesEvent = new EventEmitter();

  @Input() chatRooms: ChatRoom[];

  @Input() selectedChatRoom: ChatRoom;

  isAdmin: boolean = true;

  constructor(private chatRoomService: ChatRoomService) {}

  ngOnInit(): void {}

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

  ngOnDestroy(): void {}
}
