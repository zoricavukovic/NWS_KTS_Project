package com.example.serbUber.repository.message;

import com.example.serbUber.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

}
