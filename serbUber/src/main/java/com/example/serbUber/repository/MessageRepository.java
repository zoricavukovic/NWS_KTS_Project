package com.example.serbUber.repository;

import com.example.serbUber.model.Message;
import com.example.serbUber.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("select m from Message m left join fetch m.sender u order by m.timeStamp")
    List<Message> findAllMessages();

    @Query("select m from Message m left join fetch m.sender u where u.email =?1 order by m.timeStamp")
    List<Message> getAllMessagesPerUser(String email);

}
