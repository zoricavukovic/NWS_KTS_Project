package com.example.serbUber.controller;

import com.example.serbUber.dto.MessageDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.request.MessageRequest;
import com.example.serbUber.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import java.util.HashMap;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

@RestController()
@RequestMapping("/messages")
public class MessageController {

    private MessageService messageService;

    public MessageController(final MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public HashMap<String, List<MessageDTO>> getAll() {

        return messageService.getAll();
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public List<MessageDTO> getAllPerUser(@PathVariable @Email(message = WRONG_EMAIL) String email) {

        return messageService.getMessagesPerUser(email);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public MessageDTO create(@Valid @RequestBody MessageRequest messageRequest)
            throws EntityNotFoundException
    {

        return messageService.create(
                messageRequest.getMessage(),
                messageRequest.getSenderEmail(),
                messageRequest.getReceiverEmail(),
                messageRequest.isAdminResponse()
        );
    }

}
