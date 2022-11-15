import { Role } from "../user/role";

export class MessageUser {

    email: string;

    name: string;

    surname: string;

    role: Role;

    constructor(
        email: string,
        name: string,
        surname: string,
        role: Role
    ) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.role = role;
    }

}