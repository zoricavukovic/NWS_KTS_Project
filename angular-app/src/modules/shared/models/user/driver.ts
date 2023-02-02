import { Role } from './role';
import { Vehicle } from '../vehicle/vehicle';

export interface Driver {
  id?: number;
  email: string;
  name: string;
  surname: string;
  phoneNumber: string;
  profilePicture?: string;
  city: string;
  role?: Role;
  password: string;
  rate?: number;
  confirmPassword?: string;
  vehicle: Vehicle;
  active?: boolean;
  pageNumber?: number;
  pageSize?: number;
}

export interface DriverRegistrationRequest {
  email: string;
  name: string;
  surname: string;
  phoneNumber: string;
  city: string;
  password: string;
  confirmPassword: string;
  petFriendly: boolean;
  babySeat: boolean;
  vehicleType: string;
}
