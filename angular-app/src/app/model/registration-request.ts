import { zip } from "rxjs";

export class RegistrationRequest {
    email: string;
    password: string;
    confirmationPassword: string;
    name: string;
    surname: string;
    address:{
        city: string;
        street: string;
        number: string;
        zipCode: string;
        lon: number;
        lat: number;
    }
    phoneNumber: string;

    constructor(
        email:string,
        password:string,
        confirmationPassword: string,
        name: string,
        surname: string,
        phoneNumber: string,
        city: string,
        street: string,
        number: string,
        zipCode: string,

    ){
        this.email = email;
        this.password = password;
        this.confirmationPassword = confirmationPassword;
        this.name = name;
        this.surname = surname;
        this.address = {
            city:city,
            street:street,
            number:number,
            zipCode: zipCode,
            lon: 0,
            lat:0
        }
        this.phoneNumber = phoneNumber;
    }
}