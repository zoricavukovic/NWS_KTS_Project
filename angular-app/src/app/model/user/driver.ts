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
}
