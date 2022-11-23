import { VehicleTypeInfo } from './vehicle-type-info';
export interface Vehicle {
  id?: number;
  petFriendly: boolean;
  babySeat: boolean;
  vehicleType?: string;
  vehicleTypeInfo?: VehicleTypeInfo;
  rate?: number;
}
