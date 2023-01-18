package com.example.serbUber.dto.user;

import com.example.serbUber.model.VehicleType;
import com.example.serbUber.model.user.DriverUpdateApproval;

import java.util.LinkedList;
import java.util.List;

public class DriverUpdateApprovalDTO {

    private final Long id;
    private final String userEmail;
    private final String name;
    private final String surname;
    private final String phoneNumber;
    private final String city;
    private final boolean approved = false;
    private final VehicleType vehicleType;
    private final boolean petFriendly;
    private final boolean babySeat;

    public DriverUpdateApprovalDTO(
            final Long id,
            final String userEmail,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final VehicleType vehicleType,
            final boolean petFriendly,
            final boolean babySeat
    ) {
        this.id = id;
        this.userEmail = userEmail;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.vehicleType = vehicleType;
        this.petFriendly = petFriendly;
        this.babySeat = babySeat;
    }

    public DriverUpdateApprovalDTO(final DriverUpdateApproval approval) {
        this.id = approval.getId();
        this.userEmail = approval.getUserEmail();
        this.name = approval.getName();
        this.surname = approval.getSurname();
        this.phoneNumber = approval.getPhoneNumber();
        this.city = approval.getCity();
        this.vehicleType = approval.getVehicleType();
        this.petFriendly = approval.isPetFriendly();
        this.babySeat = approval.isBabySeat();
    }

    public static List<DriverUpdateApprovalDTO> fromApprovals(final List<DriverUpdateApproval> approvals){
        List<DriverUpdateApprovalDTO> driverUpdateApprovalDTOS = new LinkedList<>();
        approvals.forEach(approval ->
                driverUpdateApprovalDTOS.add(new DriverUpdateApprovalDTO(approval))
        );

        return driverUpdateApprovalDTOS;
    }

    public Long getId() {
        return id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public boolean isApproved() {
        return approved;
    }

    public VehicleType getVehicleType() {
        return vehicleType;
    }

    public boolean isPetFriendly() {
        return petFriendly;
    }

    public boolean isBabySeat() {
        return babySeat;
    }
}
