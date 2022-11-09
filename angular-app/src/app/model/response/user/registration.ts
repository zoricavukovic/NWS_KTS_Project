export class RegistrationResponse {
    email: string;
    password: string;
    name: string;
    surname: string;
    address: string;
    phoneNumber: string;

    constructor(
        email:string,
        password:string,
        name: string,
        surname: string,
        address: string,
        phoneNumber: string
    ){
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}