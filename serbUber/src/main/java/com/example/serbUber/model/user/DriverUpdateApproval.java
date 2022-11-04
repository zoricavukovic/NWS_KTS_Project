package com.example.serbUber.model.user;

import javax.persistence.*;

@Entity
@Table(name="driver_update_approval")
public class DriverUpdateApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="email", nullable = false, unique = true)
    private String userEmail;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="surname", nullable = false)
    private String surname;

    @Column(name="phone_number", nullable = false)
    private String phoneNumber;

    @Column(name="city", nullable = false)
    private String city;

    @Column(name="approved", nullable = false)
    private boolean approved = false;

    public DriverUpdateApproval() {}

    public DriverUpdateApproval(
            final String userEmail,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final boolean approved
    ) {
        this.userEmail = userEmail;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.approved = approved;
    }

    public DriverUpdateApproval(
            final String userEmail,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city
    ) {
        this.userEmail = userEmail;
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.city = city;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
