import { User } from "../user/user";

export class Message {
    message: string;
    timeStamp: Date;
    sender: User;
    receiver: User;
    adminResponse: boolean;

    constructor(
        message: string,
        timeStamp: Date,
        sender: User,
        receiver: User,
        adminResponse: boolean
    ) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.sender = sender;
        this.receiver = receiver;
        this.adminResponse = adminResponse;
    }

}