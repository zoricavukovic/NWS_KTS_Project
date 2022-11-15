import { Role } from "./role";
import { Vehicle } from "../vehicle";
import { User } from "./user";

export class Driver extends User {
    rate: number;
    vehicle: Vehicle;

    constructor(
        id: number,
        email: string,
        name: string,
        surname: string,
        phoneNumber: string,
        city: string,
        role: Role,
        profilePicture:string,
        rate: number,
        vehicle: Vehicle
    ) {
        super(id, email, name, surname, phoneNumber, city, role, profilePicture);
        this.vehicle = vehicle;
        this.rate = rate;
    }
}
