package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.ReservationDTO;
import com.example.serbUber.model.Route;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface IReservationService {
    List<ReservationDTO> getAll();
    ReservationDTO create(
            final LocalDateTime timeStamp,
            final Route route,
            final List<String> users
    );
}
