import { Route } from '../route/route';
import {User} from "../user/user";
import {Vehicle} from "../vehicle/vehicle";
import {VehicleTypeInfo} from "../vehicle/vehicle-type-info";

export interface DrivingNotification {
  route: Route;
  senderEmail: string;
  price: number;
  receiverEmail?: string;
  passengers?: string[];
  read?: boolean;
  drivingNotificationType?: string;
  started?: Date;
  duration?: number;
  petFriendly?: boolean;
  babySeat?: boolean;
  vehicleType?: string;
  reason?: string;
  drivingId?: number;
  notificationId?: number;
  minutes?: number;
  drivingStatus?: string;
  active?: boolean;
  chosenDateTime?: Date;
  receivers?: User[];
  vehicle?: Vehicle;
  vehicleTypeInfo?: VehicleTypeInfo;
  reservation?: boolean
}

