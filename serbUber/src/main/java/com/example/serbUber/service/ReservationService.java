package com.example.serbUber.service;

import com.example.serbUber.dto.ReservationDTO;
import com.example.serbUber.model.Reservation;
import com.example.serbUber.model.Route;
import com.example.serbUber.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.serbUber.dto.ReservationDTO.fromReservations;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;

    public ReservationService(final ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public List<ReservationDTO> getAll() {
        List<Reservation> reservations = reservationRepository.findAll();

        return fromReservations(reservations);
    }

    public ReservationDTO create(
            final LocalDateTime timeStamp,
            final Route route,
            final List<String> users
    ) {

        Reservation reservation = reservationRepository.save(new Reservation(
                timeStamp,
                route,
                users
        ));

        return new ReservationDTO(reservation);
    }

}
