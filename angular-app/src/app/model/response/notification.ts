import { User } from "./user/user";


export class Notification {

    message: String;
    sender: User;
    receiver: User;
    report: boolean;
    timeStamp: Date;

    constructor(
        message: String,
        sender: User,
        receiver: User,
        report: boolean,
        timeStamp: Date 
    ) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.report = report;
        this.timeStamp = timeStamp;
    }


}