package com.example.serbUber.service.message;

import com.example.serbUber.model.Message;
import com.example.serbUber.repository.message.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class MessageService {

    private MessageRepository messageRepository;

    public MessageService(final MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public List<Message> createMessagesList(
            final String message,
            final boolean adminResponse
    ) {
        List<Message> messages = new LinkedList<>();
        messages.add(new Message(message, adminResponse));

        return messages;
    }
}
