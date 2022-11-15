package com.example.serbUber.controller.message;

import com.example.serbUber.service.message.MessageService;
import org.springframework.web.bind.annotation.*;


@RestController()
@RequestMapping("/messages")
public class MessageController {

    private MessageService messageService;

    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

}
