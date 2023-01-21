package com.example.serbUber.repository;

import com.example.serbUber.dto.bell.BellNotificationDTO;
import com.example.serbUber.model.BellNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface BellNotificationRepository extends JpaRepository<BellNotification, Long> {

    @Query("select b from BellNotification b where b.userId=?1 order by b.timeStamp desc")
    List<BellNotification> getBellNotificationsForUser(Long id);

    @Modifying
    @Transactional
    @Query("update BellNotification b set b.seen=true where b.userId=?1")
    void setAllAsSeen(Long userId);

    @Modifying
    @Transactional
    @Query("delete from BellNotification b where b.userId=?1 and b.seen=true")
    void deleteAllSeen(Long userId);
}
