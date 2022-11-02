package com.example.serbUber.service;

import com.example.serbUber.dto.NotificationDTO;
import com.example.serbUber.model.Notification;
import com.example.serbUber.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.serbUber.dto.NotificationDTO.fromNotifications;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }


    public void create(
            final String message,
            final String sender,
            final String receiver,
            final boolean report
    ) {
        //ovde da se doda user service i zamene null sa findByEmail
        notificationRepository.save(new Notification(
            message,
            null,
            null,
            report
        ));
    }

    public List<NotificationDTO> getAll() {
        List<Notification> notifications = notificationRepository.findAll();

        return fromNotifications(notifications);
    }

    public void delete(Long id) {

        notificationRepository.deleteById(id);
    }
}
