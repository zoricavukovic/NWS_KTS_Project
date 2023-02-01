import { Route } from '../route/route';

export interface DrivingNotification {
  route: Route;
  price: number;
  passengers?: string[];
  senderEmail?: string;
  started?: Date;
  petFriendly?: boolean;
  babySeat?: boolean;
  vehicleType?: string;
  drivingId?: number;
  notificationId?: number;
  minutes?: number;
  drivingStatus?: string;
  active?: boolean;
  chosenDateTime?: Date;
  vehicleId?: number;
  reservation?: boolean;
  duration?: number;
  wrongRoute?: boolean;
  justFinished?: boolean;
}
