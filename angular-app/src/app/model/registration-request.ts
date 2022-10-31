export class RegistrationRequest {
    email: string;
    password: string;
    confirmationPassword: string;
    name: string;
    surname: string;
    city: string;
    phoneNumber: string;

    constructor(
        email:string,
        password:string,
        confirmationPassword: string,
        name: string,
        surname: string,
        phoneNumber: string,
        city: string

    ){
        this.email = email;
        this.password = password;
        this.confirmationPassword = confirmationPassword;
        this.name = name;
        this.surname = surname;
        this.city = city;
        this.phoneNumber = phoneNumber;
    }
}