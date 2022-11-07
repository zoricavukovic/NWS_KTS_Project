export class UserPasswordUpdateRequest {
    email: string;
    currentPassword: string;
    newPassword: string;
    confirmPassword: string;

    constructor(
        email: string,
        currentPassword: string,
        newPassword: string,
        confirmPassword: string
    ) {
        this.email = email;
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
        this.confirmPassword = confirmPassword;
    }
}