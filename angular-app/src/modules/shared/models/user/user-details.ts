import { Role } from './role';

export interface UserDetails {
  email: string;
  name: string;
  surname: string;
  phoneNumber?: string;
  city?: string;
  role?: Role;
  petFriendly?: boolean;
  babySeat?: boolean;
  vehicleType?: string;
}
