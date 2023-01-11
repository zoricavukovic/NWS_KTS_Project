import { User } from "../user/user";

export interface Report{
    id: number;
    message: string;
    timeStamp: Date;
    sender: User;
  }