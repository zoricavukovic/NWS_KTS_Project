package com.example.serbUber.controller;

import com.example.serbUber.dto.NotificationDTO;
import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.model.Notification;
import com.example.serbUber.request.NotificationRequest;
import com.example.serbUber.request.VehicleRequest;
import com.example.serbUber.service.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    private NotificationService notificationService;

    public NotificationController(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@Valid @RequestBody NotificationRequest notificationRequest) {

        this.notificationService.create(
            notificationRequest.getMessage(),
            notificationRequest.getSender(),
            notificationRequest.getReceiver(),
            notificationRequest.isReport()
        );
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<NotificationDTO> getAll() {

        return this.notificationService.getAll();
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable String id) {

        this.notificationService.delete(id);
    }


}
