package com.example.serbUber.service;

import com.example.serbUber.dto.MessageDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.Message;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.MessageRepository;
import com.example.serbUber.service.user.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.dto.MessageDTO.fromMessages;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    private UserService userService;

    public MessageService(
            final MessageRepository messageRepository,
            final UserService userService
    ) {
        this.messageRepository = messageRepository;
        this.userService = userService;
    }

    public HashMap<String, List<MessageDTO>> getAll() {
        List<Message> messages = messageRepository.findAllMessages();

        return generateChatRooms(messages);
    }


    private HashMap<String, List<MessageDTO>> generateChatRooms(List<Message> messages) {
        HashMap<String, List<MessageDTO>> chatRoomDTOs = new HashMap<>();

        for (Message m : messages) {
            if (chatRoomDTOs.containsKey(m.getSender().getId().toString())) {
                chatRoomDTOs.get(m.getSender().getId().toString()).add(new MessageDTO(m));
            } else {
                chatRoomDTOs.put(m.getSender().getId().toString(), new LinkedList<>());
                chatRoomDTOs.get(m.getSender().getId().toString()).add(new MessageDTO(m));
            }
        }

        return chatRoomDTOs;
    }

    public List<MessageDTO> getMessagesPerUser(String email) {
        List<Message> messages = messageRepository.getAllMessagesPerUser(email);

        return fromMessages(messages);
    }

    public MessageDTO create(
            final String message,
            final String senderEmail,
            final String receiverEmail,
            final boolean adminResponse
    ) throws EntityNotFoundException {
        User sender = userService.getUserByEmail(senderEmail);
        User receiver = findAdmin(receiverEmail);

        Message createdMessage = messageRepository.save(new Message(
                message,
                sender,
                receiver,
                adminResponse
        ));

        return new MessageDTO(createdMessage);
    }

    private User findAdmin(String receiverEmail) throws EntityNotFoundException {

        return (receiverEmail != null) ? userService.getUserByEmail(receiverEmail) :
                userService.findFirstAdmin();
    }
}
