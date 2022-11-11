import { Message } from "./message";

export class ChatRoom {

    chatRoom: Map<string, Message[]>

    constructor(
        chatRoom: Map<string, Message[]>
    ) {
        this.chatRoom = chatRoom;
    }
}