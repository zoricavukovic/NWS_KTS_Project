import { ChatRoom } from "../../response/messages/chat-room";

export class ChatRoomWithNotify {

    chatRoom: ChatRoom;

    notifyAdmin: boolean;

    constructor(
        chatRoom: ChatRoom,
        notifyAdmin: boolean
    ) {
        this.chatRoom = chatRoom;
        this.notifyAdmin = notifyAdmin;
    }

}