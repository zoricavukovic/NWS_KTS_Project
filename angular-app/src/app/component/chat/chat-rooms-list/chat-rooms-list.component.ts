import { Component, Input, OnInit } from '@angular/core';
import { ChatRoom } from 'src/app/model/response/messages/chat-room';

@Component({
  selector: 'app-chat-rooms-list',
  templateUrl: './chat-rooms-list.component.html',
  styleUrls: ['./chat-rooms-list.component.scss']
})
export class ChatRoomsListComponent implements OnInit {

  @Input() chatRooms: ChatRoom[];

  @Input() selectedChatRoom: ChatRoom;

  constructor() { }

  ngOnInit(): void {}

  changedSelected(index: number): void {
    this.selectedChatRoom = this.chatRooms[index];
  }


}
