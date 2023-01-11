import { MessageResponse } from './message-response';
import { UserDetails } from '../user/user-details';
export interface ChatRoom {
  id: number;
  resolved: boolean;
  client: UserDetails;
  admin: UserDetails;
  messages: MessageResponse[];
}
