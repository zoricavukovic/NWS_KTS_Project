package com.example.serbUber.dto.user;

import com.example.serbUber.model.user.RegularUser;

import java.util.LinkedList;
import java.util.List;

public class RegularUserPageDTO extends RegularUserDTO{
    private int pageSize;
    private int pageNumber;

    public RegularUserPageDTO(RegularUser regularUser, int pageSize, int pageNumber) {
        super(regularUser);
        this.pageSize = pageSize;
        this.pageNumber = pageNumber;
    }

    public static List<RegularUserPageDTO> fromRegularUsersPage(final List<RegularUser> regularUsers, final int pageSize, final int pageNumber){
        List<RegularUserPageDTO> regularUserDTOs = new LinkedList<>();
        for (RegularUser regularUser : regularUsers) {
            RegularUserPageDTO regularUserPageDTO = new RegularUserPageDTO(regularUser, pageSize, pageNumber);
            regularUserPageDTO.setProfilePicture(regularUserPageDTO.getProfilePicture());

            regularUserDTOs.add(regularUserPageDTO);
        }

        return regularUserDTOs;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }
}
