package com.example.serbUber.controller;

import com.example.serbUber.dto.ReservationDTO;
import com.example.serbUber.request.ReservationRequest;
import com.example.serbUber.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(final ReservationService reservationService) { this.reservationService = reservationService; }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<ReservationDTO> getAll() {

        return this.reservationService.getAll();
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationDTO create(@Valid @RequestBody ReservationRequest reservationRequest) {
        /* TODO izmenjen request da ne prima route vec routeReq
        return this.reservationService.create(
                reservationRequest.getTimeStamp(),
                reservationRequest.getRoute(),
                reservationRequest.getUsers()
        );*/
        return null;
    }
}
