import { Message } from "./message";
import { MessageUser } from "./message-user";

export class ChatRoom {

    id: number;

    resolved: boolean;

    client: MessageUser;

    admin: MessageUser;

    messages: Message[];

    constructor(
        id: number,
        resolved: boolean,
        client: MessageUser,
        admin: MessageUser,
        messages: Message[]
    ) {
        this.id = id;
        this.resolved = resolved;
        this.client = client;
        this.admin = admin;
        this.messages = messages;
    }
}