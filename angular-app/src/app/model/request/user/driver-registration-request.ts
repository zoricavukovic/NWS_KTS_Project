import { Role } from '../../response/user/role';
import { VehicleRequest } from '../vehicle-request';

export interface Driver {
  id?: number;
  email: string;
  name: string;
  surname: string;
  phoneNumber: string;
  city: string;
  role?: Role;
  password: string;
  confirmPassword?: string;
  rate: number;
  vehicleRequest: VehicleRequest;
}
