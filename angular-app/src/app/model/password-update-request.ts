export class PasswordUpdateRequest {
    email: string;
    newPassword: string;
    confirmPassword: string;

    constructor(
        email: string,
        newPassword: string,
        confirmPassword: string
    ){
        this.email = email;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
}
