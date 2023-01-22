import {Route} from "../route/route";
import {User} from "../user/user";
import {VehicleTypeInfo} from "../vehicle/vehicle-type-info";

export interface DrivingNotificationResponse {
  route: Route;
  passengers: User[];
  drivingNotificationType: string;
  started: Date;
  chosenDateTime: Date
  vehicleTypeInfo: VehicleTypeInfo;
  price: number;
}
