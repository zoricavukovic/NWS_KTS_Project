import { Role } from "./role";
import { Vehicle } from "../vehicle";

export class Driver {
    name: string;
    surname: string;
    email: string;
    password: string;
    phoneNumber: string;
    city: string;
    profilePicture: string;
    role: Role;
    rate: number;
    vehicle: Vehicle;
}
