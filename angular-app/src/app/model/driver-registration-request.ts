import { VehicleRequest } from "./vehicle-request";

export class DriverRegistrationRequest {
    email: string;
    password: string;
    confirmPassword: string;
    name: string;
    surname: string;
    phoneNumber: string;
    city: string;
    vehicleRequest: VehicleRequest;

    constructor(
        email: string,
        password: string,
        confirmPassword: string,
        name: string,
        surname: string,
        phoneNumber: string,
        city: string,
        vehicleRequest: VehicleRequest
    ){
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.vehicleRequest = vehicleRequest;
    }

}