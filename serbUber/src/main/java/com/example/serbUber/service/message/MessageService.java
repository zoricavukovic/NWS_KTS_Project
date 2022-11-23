package com.example.serbUber.service.message;

import com.example.serbUber.model.Message;
import com.example.serbUber.repository.message.MessageRepository;
import com.example.serbUber.service.interfaces.IMessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Component
@Qualifier("messageServiceConfiguration")
public class MessageService implements IMessageService {

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
