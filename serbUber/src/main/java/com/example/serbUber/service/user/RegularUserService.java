package com.example.serbUber.service.user;

import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.dto.VerifyDTO;
import com.example.serbUber.dto.user.RegistrationDTO;
import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.dto.user.RegularUserPageDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.user.RegularUserRepository;
import com.example.serbUber.service.EmailService;
import com.example.serbUber.service.RouteService;
import com.example.serbUber.service.VerifyService;
import com.example.serbUber.service.WebSocketService;
import com.example.serbUber.service.interfaces.IRegularUserService;
import com.example.serbUber.service.payment.TokenBankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.RouteDTO.fromRoutes;
import static com.example.serbUber.dto.user.RegularUserDTO.fromRegularUsers;
import static com.example.serbUber.dto.user.RegularUserPageDTO.fromRegularUsersPage;
import static com.example.serbUber.exception.ErrorMessagesConstants.UNBLOCK_UNBLOCKED_USER_MESSAGE;
import static com.example.serbUber.model.DrivingStatus.ACCEPTED;
import static com.example.serbUber.util.Constants.ROLE_REGULAR_USER;
import static com.example.serbUber.util.Constants.getProfilePicture;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;
import static com.example.serbUber.util.PictureHandler.convertPictureToBase64ByName;

@Component
@Qualifier("regularUserServiceConfiguration")
public class RegularUserService implements IRegularUserService {

    private RegularUserRepository regularUserRepository;
    private VerifyService verifyService;
    private RoleService roleService;
    private RouteService routeService;
    private WebSocketService webSocketService;
    private TokenBankService tokenBankService;
    private EmailService emailService;

    @Autowired
    public RegularUserService(
            final RegularUserRepository regularUserRepository,
            final VerifyService verifyService,
            final RouteService routeService,
            final RoleService roleService,
            final WebSocketService webSocketService,
            final TokenBankService tokenBankService,
            final EmailService emailService
    ) {
        this.regularUserRepository = regularUserRepository;
        this.verifyService = verifyService;
        this.routeService = routeService;
        this.roleService = roleService;
        this.webSocketService = webSocketService;
        this.tokenBankService = tokenBankService;
        this.emailService = emailService;
    }

    public List<RegularUserDTO> getAll() {
        List<RegularUser> regularUsers = regularUserRepository.findAll();

        List<RegularUserDTO> regularUserDTOs = fromRegularUsers(regularUsers);
        regularUserDTOs.forEach(user ->  user.setProfilePicture(convertPictureToBase64ByName(user.getProfilePicture())));

        return regularUserDTOs;
    }

    public RegularUserDTO get(final Long id) throws EntityNotFoundException {
        Optional<RegularUser> optionalRegularUser = regularUserRepository.findById(id);

        return optionalRegularUser.map(RegularUserDTO::new)
            .orElseThrow(() ->  new EntityNotFoundException(id, EntityType.USER));
    }

    public RegularUser getRegularById(final Long id) throws EntityNotFoundException {

        return regularUserRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.USER));
    }

    public RegularUser getRegularByEmail(final String email) throws EntityNotFoundException {

        return regularUserRepository.getRegularUserByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException(email, EntityType.USER));
    }

    public RegistrationDTO registerRegularUser(
            final String email,
            final String password,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture
    ) throws EntityAlreadyExistsException, EntityNotFoundException {
        try {
            String hashedPassword = getHashedNewUserPassword(password);
            RegularUser regularUser = regularUserRepository.save(new RegularUser(
                email,
                hashedPassword,
                name,
                surname,
                phoneNumber,
                city,
                getProfilePicture(profilePicture),
                roleService.get(ROLE_REGULAR_USER)
            ));
            VerifyDTO verifyDTO = verifyService.create(regularUser.getId(), regularUser.getEmail());
            this.tokenBankService.createTokenBank(regularUser);

            return new RegistrationDTO(verifyDTO.getId(), email);
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException(ROLE_REGULAR_USER, EntityType.ROLE);
        } catch (Exception e) {
            throw new EntityAlreadyExistsException(String.format("User with %s already exists.", email));
        }
    }

    public boolean updateFavouriteRoutes(final Long userId, final Long routeId) throws EntityNotFoundException {
        RegularUser user = getRegularById(userId);
        Route route  = routeService.get(routeId);
        List<Route> favouriteRoutes = user.getFavouriteRoutes();
        if(favouriteRoutes.contains(route)){
            favouriteRoutes.remove(route);
        }
        else{
            favouriteRoutes.add(route);
        }
        user.setFavouriteRoutes(favouriteRoutes);
        regularUserRepository.save(user);
        return true;
    }


    public boolean isFavouriteRoute(final Long routeId, final Long userId) {
        RegularUser u = regularUserRepository.getUserWithFavouriteRouteId(routeId, userId);
        return u != null;
    }

    public List<RouteDTO> getFavouriteRoutes(final Long id) throws EntityNotFoundException {
        Optional<RegularUser> u = regularUserRepository.findById(id);
        if(u.isPresent()){
            return fromRoutes(u.get().getFavouriteRoutes());
        }
        throw new EntityNotFoundException(id, EntityType.USER);
    }

    public boolean blockRegular(final Long id, final String reason)
            throws IOException, EntityNotFoundException, EntityUpdateException, MailCannotBeSentException {
        RegularUser regularUser = getRegularById(id);
        if (regularUserInActiveDriving(regularUser.getDrivings())) {
            throw new EntityUpdateException("Regular user cannot be blocked while in active driving.");
        }

        regularUser.setOnline(false);
        regularUser.setBlocked(true);
        regularUser.setVerified(false);
        regularUserRepository.save(regularUser);
        emailService.sendBlockDriverMail(regularUser.getEmail(), reason);
        this.webSocketService.sendBlockedNotification(regularUser.getEmail());

        return true;
    }

    public boolean getIsBlocked(final Long id) {

        return regularUserRepository.getIsBlocked(id);
    }

    public boolean unblock(final Long id)
            throws EntityNotFoundException, EntityUpdateException {
        RegularUser regularUser = getRegularById(id);
        if (!regularUser.isBlocked()) {
            throw new EntityUpdateException(UNBLOCK_UNBLOCKED_USER_MESSAGE);
        }
        regularUser.setBlocked(false);
        regularUser.setVerified(true);
        regularUserRepository.save(regularUser);

        return true;
    }

    private boolean regularUserInActiveDriving(final List<Driving> drivings) {
        for (Driving driving : drivings) {
            if (driving.isActive() || (driving.getDrivingStatus() == ACCEPTED && driving.getStarted().isAfter(LocalDateTime.now()))) {
                return true;
            }
        }

        return false;
    }

    public Page<RegularUser> getRegularUserPage(Pageable page){
        return regularUserRepository.findAll(page);
    }
    public List<RegularUserPageDTO> getRegularUsersWithPagination(int pageNumber, int pageSize) {
        Pageable page = PageRequest.of(pageNumber, pageSize);
        Page<RegularUser> results = getRegularUserPage(page);
        return fromRegularUsersPage(results.getContent(), results.getSize(), results.getTotalPages());
    }

}
