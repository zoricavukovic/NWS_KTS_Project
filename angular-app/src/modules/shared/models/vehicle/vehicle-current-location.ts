import {Location} from "../route/location";

export interface VehicleCurrentLocation {
  id: number;
  currentLocation: Location;
  inDrive: boolean;
  type: string;
  driverId: number;
  activeDriver:boolean;
  crossedWaypoints: number;
  timeToDestination: number;
}
