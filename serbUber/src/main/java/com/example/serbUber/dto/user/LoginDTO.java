package com.example.serbUber.dto.user;

public class LoginDTO {

    private String token;
    private UserDTO userDTO;

    public LoginDTO(final String token, final UserDTO userDTO) {
        this.token = token;
        this.userDTO = userDTO;
        this.userDTO.setProfilePicture(userDTO.getProfilePicture());
    }

    public String getToken() {
        return token;
    }

    public void setToken(final String token) {
        this.token = token;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(final UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
