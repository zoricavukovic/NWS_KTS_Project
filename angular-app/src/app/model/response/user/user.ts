import { Role } from './role';

export class User {
  id: number;
  email: string;
  name: string;
  surname: string;
  phoneNumber: string;
  city: string;
  role: Role;
  profilePicture: string;

  constructor(
    id: number,
    email: string,
    name: string,
    surname: string,
    phoneNumber: string,
    city: string,
    role: Role,
    profilePicture: string
  ) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.surname = surname;
    this.phoneNumber = phoneNumber;
    this.city = city;
    this.role = role;
    this.profilePicture = profilePicture;
  }

  isDriver(): boolean {
    return this.role.name === 'ROLE_DRIVER';
  }
}
