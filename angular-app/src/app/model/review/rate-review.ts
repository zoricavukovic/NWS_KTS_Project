import { Driving } from "../driving/driving";
import { RegularUser } from "../user/regular-user";

export interface RateReview {
  vehicleRate: number;
  driverRate: number;
  message: string;
  driving: number;
  userEmail?: string;
  id?: number;
}

export interface Review{
  id: number;
  vehicleRate: number;
  driverRate: number;
  message: string;
  driving: Driving;
  sender: RegularUser;
}
