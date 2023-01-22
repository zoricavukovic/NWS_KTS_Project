import {Route} from "../route/route";

export interface SimpleDrivingInfo {
  drivingId: number;
  minutes: number;
  route: Route;
  active: boolean;
  cost: number;
  drivingStatus: string;
  vehicleId: number;
}
