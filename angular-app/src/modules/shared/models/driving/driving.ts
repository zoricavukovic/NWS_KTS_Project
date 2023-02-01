import { Route } from '../route/route';
import { User } from '../user/user';

export class Driving {
  id?: number;
  active: boolean;
  duration: number;
  started: Date;
  route: Route;
  drivingStatus: string;
  driverId: number;
  users: User[];
  price: number;
  hasReviewForUser: boolean;
  pageSize?: number;
  pageNumber?: number;
  isFavourite?: boolean;
}
