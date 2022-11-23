package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.message.ChatRoomDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.NoAvailableAdminException;
import com.example.serbUber.model.ChatRoom;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IChatRoomService {
    List<ChatRoomDTO> getAllChatRooms(final String email) throws EntityNotFoundException;
    ChatRoomDTO getActiveChatRoom(final String email) throws EntityNotFoundException;
    ChatRoom getActiveChatRoomById(final Long id) throws EntityNotFoundException;
    ChatRoomDTO create(
            final Long chatId,
            final String message,
            final String senderEmail,
            final String receiverEmail,
            final boolean adminResponse
    ) throws NoAvailableAdminException, EntityNotFoundException;
    ChatRoomDTO resolve(final Long id) throws EntityNotFoundException;
    ChatRoomDTO setMessagesToSeen(final Long chatRoomId, final boolean adminLogged) throws EntityNotFoundException;
}
