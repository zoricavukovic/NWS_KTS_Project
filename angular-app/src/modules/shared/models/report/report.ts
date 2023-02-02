import { Driver } from "../user/driver";
import { User } from "../user/user";

export interface Report{
    id: number;
    message: string;
    timeStamp: Date;
    sender: User;
  }

export interface BehaviourReportDialogData {
  currentUser: User;
  driver: Driver;
  userToReport: User;
}

export interface BehaviourReportRequest {
  senderId: number;
  receiverId?: number;
  drivingId?: number;
  message: string;
}