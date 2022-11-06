export class UsersProfileUpdateRequest {
    private email: string;
    private name: string;
    private surname: string;
    private phoneNumber: string
    private city: string;

    constructor(
        email: string,
        name: string,
        surname: string,
        phoneNumber: string,
        city: string
    ) {
        this.email = email;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
    }
}