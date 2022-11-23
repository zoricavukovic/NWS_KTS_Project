package com.example.serbUber.controller.message;

import com.example.serbUber.service.message.MessageService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/messages")
public class MessageController {

    private MessageService messageService;

    public MessageController(@Qualifier("messageServiceConfiguration") final MessageService messageService) {
        this.messageService = messageService;
    }

}
