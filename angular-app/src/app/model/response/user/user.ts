import { Role } from './role';

const ROLE_ADMIN = 'ROLE_ADMIN';
const ROLE_REGULAR_USER = 'ROLE_REGULAR_USER';
const ROLE_DRIVER = 'ROLE_DRIVER';

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

  isUserAdmin(): boolean {
    return this.role.name === ROLE_ADMIN;
  }

  userIsRegular(): boolean {
    return this.role.name === ROLE_REGULAR_USER;
  }

  userIsDriver(): boolean {
    return this.role.name === ROLE_DRIVER;
  }
}
