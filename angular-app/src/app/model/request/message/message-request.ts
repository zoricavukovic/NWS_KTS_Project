
export class MessageRequest {
    chatId: number;
    message: string;
    senderEmail: string;
    receiverEmail: string;
    adminResponse: boolean;

    constructor(
        chatId: number,
        message: string,
        sender: string,
        receiver: string,
        adminResponse: boolean
    ) {
        this.chatId = chatId;
        this.message = message;
        this.senderEmail = sender;
        this.receiverEmail = receiver;
        this.adminResponse = adminResponse;
    }
}

export class MessageSeenRequest {
    chatRoomId: number;
    adminLogged: boolean;

    constructor(
        chatRoomId: number,
        adminLogged: boolean
    ) {
        this.chatRoomId = chatRoomId;
        this.adminLogged = adminLogged;
    }
}