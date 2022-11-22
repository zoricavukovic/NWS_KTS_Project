package com.example.serbUber.service.message;

import com.example.serbUber.dto.message.ChatRoomDTO;
import com.example.serbUber.exception.AddingMessageToResolvedChatRoom;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.exception.NoAvailableAdminException;
import com.example.serbUber.model.ChatRoom;
import com.example.serbUber.model.Message;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.message.ChatRoomRepository;
import com.example.serbUber.service.interfaces.IChatRoomService;
import com.example.serbUber.service.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.message.ChatRoomDTO.fromChatRooms;

@Component
@Qualifier("chatRoomServiceConfiguration")
public class ChatRoomService implements IChatRoomService {

    private ChatRoomRepository chatRoomRepository;

    private UserService userService;

    private MessageService messageService;

    public ChatRoomService(
            final ChatRoomRepository chatRoomRepository,
            final UserService userService,
            final MessageService messageService
    ) {
        this.userService = userService;
        this.chatRoomRepository = chatRoomRepository;
        this.messageService = messageService;
    }

    public List<ChatRoomDTO> getAllChatRooms(final String email) throws EntityNotFoundException {
        User user = userService.getUserByEmail(email);

        return fromChatRooms(chatRoomRepository.getAllChatRooms(email));
    }

    //ako ne postoji trenutno aktivni chat room za usera, vrati null
    public ChatRoomDTO getActiveChatRoom(final String email) {
        Optional<ChatRoom> optionalChatRoom = chatRoomRepository.getActiveChatRoom(email);

        return (optionalChatRoom.map(ChatRoomDTO::new).orElse(null));
    }

    public ChatRoom getActiveChatRoomById(final Long id) throws EntityNotFoundException {

        return chatRoomRepository.getActiveChatRoomById(id)
                .orElseThrow(() -> new EntityNotFoundException(id.toString(), EntityType.CHAT_ROOM));
    }


    public ChatRoomDTO create(
            final Long chatId,
            final String message,
            final String senderEmail,
            final String receiverEmail,
            final boolean adminResponse
    ) throws NoAvailableAdminException, EntityNotFoundException {

        return (chatRoomNotExists(chatId)) ? createNewChatRoom(message, senderEmail, adminResponse)
                : addMessageToExisting(chatId, message, senderEmail, receiverEmail, adminResponse);
    }

    public ChatRoomDTO resolve(final Long id) throws EntityNotFoundException {
        ChatRoom chatRoom = getActiveChatRoomById(id);
        chatRoom.setResolved(true);

        return new ChatRoomDTO(chatRoomRepository.save(chatRoom));
    }

    private ChatRoomDTO addMessageToExisting(Long chatId, String message, String senderEmail, String receiverEmail, boolean adminResponse)
            throws EntityNotFoundException
    {
        User client = userService.getUserByEmail(senderEmail);
        User admin = userService.getUserByEmail(receiverEmail);
        Message newMessage = new Message(message, adminResponse);
        ChatRoom chatRoom = getActiveChatRoomById(chatId);

        chatRoom.getMessages().add(newMessage);

        return new ChatRoomDTO(chatRoomRepository.save(chatRoom));
    }

    public ChatRoomDTO setMessagesToSeen(final Long chatRoomId, final boolean adminLogged)
            throws EntityNotFoundException
    {
        ChatRoom chatRoom = getActiveChatRoomById(chatRoomId);
        chatRoom.getMessages().forEach(message -> {
            if (adminLogged && adminSawClientMessage(message)) {
                message.setSeen(true);
            } else if (!adminLogged && clientSawAdminMessage(message)) {
                message.setSeen(true);
            }
        });

        return new ChatRoomDTO(chatRoomRepository.save(chatRoom));
    }

    private ChatRoomDTO createNewChatRoom(String message, String senderEmail, boolean adminResponse)
            throws NoAvailableAdminException, EntityNotFoundException
    {
        User admin = userService.findOnlineAdmin();
        User client = userService.getUserByEmail(senderEmail);
        List<Message> messageList = messageService.createMessagesList(message, adminResponse);

        return new ChatRoomDTO(chatRoomRepository.save(new ChatRoom(
                client,
                admin,
                messageList,
                adminResponse
        )));
    }

    private boolean chatRoomNotExists(Long chatId) {
        return chatId == null;
    }

    private boolean adminSawClientMessage(final Message message) {

        return !message.isSeen() && !message.isAdminResponse();
    }

    private boolean clientSawAdminMessage(final Message message) {

        return !message.isSeen() && message.isAdminResponse();
    }

}
