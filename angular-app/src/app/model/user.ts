import { Role } from "./role";

export class User {
    email: string;
    name: string;
    surname: string;
    phoneNumber: string;
    city: string;
    role: Role;
    profilePicture: string;

    constructor(
        email: string,
        name: string,
        surname: string,
        phoneNumber: string,
        city: string,
        role: Role,
        profilePicture:string
    ) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.role = role;
        this.profilePicture = profilePicture;
    }

}
