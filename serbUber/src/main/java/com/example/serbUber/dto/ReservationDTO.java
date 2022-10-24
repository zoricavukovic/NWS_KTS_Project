package com.example.serbUber.dto;

import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Reservation;
import com.example.serbUber.model.Route;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class ReservationDTO {
    private LocalDateTime timeStamp;
    private Route route;
    private List<String> users;

    public ReservationDTO(final Reservation reservation) {
        this.timeStamp = reservation.getTimeStamp();
        this.route = reservation.getRoute();
        this.users = reservation.getUsers();
    }

    public static List<ReservationDTO> fromReservations(final List<Reservation> reservations){
        List<ReservationDTO> reservationDTOs = new LinkedList<>();
        reservations.forEach(reservation ->
                reservationDTOs.add(new ReservationDTO(reservation))
        );

        return reservationDTOs;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public Route getRoute() {
        return route;
    }

    public List<String> getUsers() {
        return users;
    }
}
