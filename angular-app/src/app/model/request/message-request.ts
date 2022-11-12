
export class MessageRequest {
    message: string;
    senderEmail: string;
    receiverEmail: string;
    adminResponse: boolean;

    constructor(
        message: string,
        sender: string,
        receiver: string,
        adminResponse: boolean
    ) {
        this.message = message;
        this.senderEmail = sender;
        this.receiverEmail = receiver;
        this.adminResponse = adminResponse;
    }
}