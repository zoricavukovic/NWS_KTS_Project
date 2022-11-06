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