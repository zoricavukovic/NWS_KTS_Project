import { Route } from './route';
import { User } from './user/user';

export class Driving {
  id: number;
  active: boolean;
  duration: number;
  started: Date;
  payingLimit: Date;
  route: Route;
  drivingStatus: string;
  driverId: number;
  usersPaid: {};
  users: User[];
  price: number;
  hasReviewForUser: boolean;
}
