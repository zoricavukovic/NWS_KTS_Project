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

export class UserProfilePictureRequest {
    email: string;
    profilePicture: string;

    constructor(
        email: string,
        profilePicture: string
    ) {
        this.email = email;
        this.profilePicture = profilePicture;
    }

}

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
