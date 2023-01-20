package com.example.serbUber.service;

import com.example.serbUber.dto.bell.BellNotificationDTO;
import com.example.serbUber.model.BellNotification;
import com.example.serbUber.repository.BellNotificationRepository;
import com.example.serbUber.service.interfaces.IBellNotificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;


import java.time.LocalDateTime;
import java.util.List;

import static com.example.serbUber.dto.bell.BellNotificationDTO.fromBellNotifications;

@Component
@Qualifier("bellNotificationServiceConfiguration")
public class BellNotificationService implements IBellNotificationService {

    private final BellNotificationRepository bellNotificationRepository;

    public BellNotificationService(final BellNotificationRepository bellNotificationRepository) {
        this.bellNotificationRepository = bellNotificationRepository;
    }

    public List<BellNotificationDTO> getBellNotificationsForUser(final Long id) {

        return fromBellNotifications(bellNotificationRepository.getBellNotificationsForUser(id));
    }

    public boolean setAllAsSeen(final Long userId) {
        bellNotificationRepository.setAllAsSeen(userId);

        return true;
    }

    public BellNotificationDTO saveBellNotification(
            final String message,
            final boolean shouldRedirect,
            final String redirectId,
            final Long userId
    ) {

        return new BellNotificationDTO(this.bellNotificationRepository.save(new BellNotification(
                message,
                LocalDateTime.now(),
                false, shouldRedirect,
                redirectId,
                userId))
        );
    }

    public boolean deleteAllSeen(final Long userId) {
        this.bellNotificationRepository.deleteAllSeen(userId);

        return true;
    }
}
