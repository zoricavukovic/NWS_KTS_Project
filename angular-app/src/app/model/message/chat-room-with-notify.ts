import { ChatRoom } from './chat-room';

export interface ChatRoomWithNotify {
  chatRoom: ChatRoom;
  notifyAdmin: boolean;
}
