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
  online?: boolean;

  constructor(
    id: number,
    email: string,
    name: string,
    surname: string,
    phoneNumber: string,
    city: string,
    role: Role,
    profilePicture: string,
    online?: boolean
  ) {
    this.id = id;
    this.email = email;
    this.name = name;
    this.surname = surname;
    this.phoneNumber = phoneNumber;
    this.city = city;
    this.role = role;
    this.profilePicture = profilePicture;
    if (online) {
      this.online = online;
    }
  }

}
