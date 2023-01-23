package com.example.serbUber.controller.message;

import com.example.serbUber.dto.message.ChatRoomDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityUpdateException;
import com.example.serbUber.exception.NoAvailableAdminException;
import com.example.serbUber.request.message.MessageRequest;
import com.example.serbUber.request.message.MessageSeenRequest;
import com.example.serbUber.service.message.ChatRoomService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.WRONG_EMAIL;

@RestController()
@RequestMapping("/chat-rooms")
public class ChatRoomController {

    private ChatRoomService chatRoomService;

    public ChatRoomController(@Qualifier("chatRoomServiceConfiguration") final ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }


    @GetMapping("/all/{email}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<ChatRoomDTO> getAllChatRooms(@Valid @Email(message = WRONG_EMAIL) @PathVariable String email)
            throws EntityNotFoundException
    {

        return chatRoomService.getAllChatRooms(email);
    }

    @GetMapping("/{email}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public ChatRoomDTO getActiveChatRoom(@Valid @Email(message = WRONG_EMAIL) @PathVariable String email)
            throws EntityNotFoundException
    {

        return chatRoomService.getActiveChatRoom(email);
    }

    @PostMapping("/resolve")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ChatRoomDTO resolve(@Valid @NotNull(message = "Id cannot be empty.") @RequestBody Long id)
            throws EntityNotFoundException, EntityUpdateException {

        return chatRoomService.resolve(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public ChatRoomDTO create(@Valid @RequestBody MessageRequest messageRequest)
            throws NoAvailableAdminException, EntityNotFoundException {

        return chatRoomService.create(
                messageRequest.getChatId(),
                messageRequest.getMessage(),
                messageRequest.getSenderEmail(),
                messageRequest.getReceiverEmail(),
                messageRequest.isAdminResponse()
        );
    }

    @PostMapping("/seen-messages")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public ChatRoomDTO setMessagesToSeen(@Valid @RequestBody MessageSeenRequest messageSeenRequest)
            throws NoAvailableAdminException, EntityNotFoundException {

        return chatRoomService.setMessagesToSeen(
                messageSeenRequest.getChatRoomId(),
                messageSeenRequest.isAdminLogged()
        );
    }


}
