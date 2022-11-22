package com.example.serbUber.service.interfaces;

import com.example.serbUber.model.Message;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IMessageService {
    List<Message> createMessagesList(
            final String message,
            final boolean adminResponse
    );
}
