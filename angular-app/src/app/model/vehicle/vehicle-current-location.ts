import {Location} from "../route/location";

export interface VehicleCurrentLocation {
  id: number;
  currentLocation: Location;
  inDrive: boolean;
}
