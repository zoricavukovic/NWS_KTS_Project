import { VehicleTypeInfo } from "./vehicle-type-info-response";

export class Vehicle{
    id: number;
    babySeat: boolean;
    petFriendly: boolean;
    vehicle_type_info: VehicleTypeInfo;
    rate: number;
}