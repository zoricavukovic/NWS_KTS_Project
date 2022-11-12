package com.example.serbUber.controller.message;

import com.example.serbUber.dto.message.ChatRoomDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.NoAvailableAdminException;
import com.example.serbUber.request.message.MessageRequest;
import com.example.serbUber.service.message.ChatRoomService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

@RestController()
@RequestMapping("/chat-rooms")
public class ChatRoomController {

    private ChatRoomService chatRoomService;

    public ChatRoomController(final ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ChatRoomDTO getActiveChatRoom(@Valid @Email(message = WRONG_EMAIL) @PathVariable String email)
            throws EntityNotFoundException
    {

        return chatRoomService.getActiveChatRoom(email);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ChatRoomDTO create(@Valid @RequestBody MessageRequest messageRequest)
            throws NoAvailableAdminException, EntityNotFoundException
    {

        return chatRoomService.create(
                messageRequest.getChatId(),
                messageRequest.getMessage(),
                messageRequest.getSenderEmail(),
                messageRequest.getReceiverEmail(),
                messageRequest.isAdminResponse()
        );
    }


}
