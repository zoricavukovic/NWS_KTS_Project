import { VehicleTypeInfo } from './vehicle-type-info';
import {Route} from "../route/route";

export interface Vehicle {
  id?: number;
  petFriendly: boolean;
  babySeat: boolean;
  vehicleType?: string;
  vehicleTypeInfo?: VehicleTypeInfo;
  rate?: number;
  location_index?: number;
  activeRoute?: Route;
}
