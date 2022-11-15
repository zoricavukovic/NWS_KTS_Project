package com.example.serbUber.repository.message;

import com.example.serbUber.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select c from ChatRoom c where c.admin.email = ?1 order by c.resolved")
    List<ChatRoom> getAllChatRooms(String email);

    @Query("select c from ChatRoom c where c.client.email = ?1 and c.resolved = false")
    Optional<ChatRoom> getActiveChatRoom(String email);

    @Query("select c from ChatRoom c where c.id = ?1 and c.resolved = false")
    Optional<ChatRoom> getActiveChatRoomById(Long id);
}
