import {Location} from "../route/location";

export interface SimpleDrivingInfo {
  drivingId: number;
  minutes: number;
  startLocation: Location;
  endLocation: Location;
  active: boolean;
  cost: number;
}
