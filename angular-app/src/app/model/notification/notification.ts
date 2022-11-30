import { User } from '../user/user';

export interface Notification {
  message: string;
  sender?: User;
  receiver?: User;
  report?: boolean;
  timeStamp?: Date;
}
