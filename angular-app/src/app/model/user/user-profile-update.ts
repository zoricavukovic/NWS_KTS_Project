export interface PasswordUpdateRequest {
  email: string;
  newPassword: string;
  confirmPassword: string;
  currentPassword?: string;
}

export interface UserProfilePictureRequest {
  email: string;
  profilePicture: string;
}
