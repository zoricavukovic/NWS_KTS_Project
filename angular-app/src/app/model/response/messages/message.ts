import { User } from "../user/user";

export class Message {
    message: string;
    timeStamp: Date;
    adminResponse: boolean;

    constructor(
        message: string,
        timeStamp: Date,
        adminResponse: boolean
    ) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.adminResponse = adminResponse;
    }

}