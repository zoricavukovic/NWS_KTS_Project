import { Role } from './role';
export interface RegularUser {
  id?: number;
  email: string;
  name: string;
  surname: string;
  city: string;
  phoneNumber: string;
  profilePicture?: string;
  role?: Role;
  password: string;
  confirmPassword?: string;
  pageNumber?: number;
  pageSize?: number;
  online?: boolean;
}
