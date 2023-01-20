package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.bell.BellNotificationDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IBellNotificationService {
    List<BellNotificationDTO> getBellNotificationsForUser(final Long id);

    boolean setAllAsSeen(final Long userId);

    BellNotificationDTO saveBellNotification(
            final String message,
            final boolean shouldRedirect,
            final String redirectId,
            final Long userId
    );

    boolean deleteAllSeen(final Long userId);
}
