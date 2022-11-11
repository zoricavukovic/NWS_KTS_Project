package com.example.serbUber.model;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.model.user.User;

import javax.persistence.*;


@Entity
@Table(name="reviews")
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="vehicle_rate", nullable = false)
    private double vehicleRate;

    @Column(name="driver_rate", nullable = false)
    private double driverRate;

    @Column(name="message")
    private String message;

    @OneToOne()
    @JoinColumn(name="sender_id", referencedColumnName = "id")
    private RegularUser sender;

    @OneToOne()
    @JoinColumn(name = "driving_id", referencedColumnName = "id")
    private Driving driving;

    public Review(){

    }
    public Review(
        final double vehicleRate,
        final double driverRate,
        final String message,
        final Driving driving,
        final RegularUser sender
    ) {
        this.vehicleRate = vehicleRate;
        this.driverRate = driverRate;
        this.message = message;
        this.driving = driving;
        this.sender = sender;
    }

    public Long getId() {
        return id;
    }

    public double getVehicleRate() {
        return vehicleRate;
    }

    public void setVehicleRate(double vehicleRate) {
        this.vehicleRate = vehicleRate;
    }

    public double getDriverRate() {
        return driverRate;
    }

    public void setDriverRate(double driverRate) {
        this.driverRate = driverRate;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Driving getDriving() {
        return driving;
    }

    public void setDriving(Driving driving) {
        this.driving = driving;
    }

    public RegularUser getSender() {
        return sender;
    }

    public void setSender(RegularUser sender) {
        this.sender = sender;
    }
}
