
export class Message {
    message: string;
    timeStamp: Date;
    adminResponse: boolean;
    seen: boolean;

    constructor(
        message: string,
        timeStamp: Date,
        adminResponse: boolean,
        seen: boolean
    ) {
        this.message = message;
        this.timeStamp = timeStamp;
        this.adminResponse = adminResponse;
        this.seen = seen;
    }

}