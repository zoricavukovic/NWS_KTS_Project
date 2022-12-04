package com.example.serbUber.service.user;

import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.dto.VerifyDTO;
import com.example.serbUber.dto.user.RegistrationDTO;
import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.user.RegularUserRepository;
import com.example.serbUber.service.RouteService;
import com.example.serbUber.service.VerifyService;
import com.example.serbUber.service.WebSocketService;
import com.example.serbUber.service.interfaces.IRegularUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.RouteDTO.fromRoutes;
import static com.example.serbUber.dto.user.RegularUserDTO.fromRegularUsers;
import static com.example.serbUber.exception.ErrorMessagesConstants.UNBLOCK_UNBLOCKED_USER_MESSAGE;
import static com.example.serbUber.util.Constants.ROLE_REGULAR_USER;
import static com.example.serbUber.util.Constants.getProfilePicture;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;
import static com.example.serbUber.util.PictureHandler.convertPictureToBase64ByName;

@Component
@Qualifier("regularUserServiceConfiguration")
public class RegularUserService implements IRegularUserService {

    private final RegularUserRepository regularUserRepository;
    private final VerifyService verifyService;
    private final RoleService roleService;
    private final RouteService routeService;
    private final WebSocketService webSocketService;

    public RegularUserService(
            final RegularUserRepository regularUserRepository,
            final VerifyService verifyService,
            final RouteService routeService,
            final RoleService roleService,
            final WebSocketService webSocketService
    ) {
        this.regularUserRepository = regularUserRepository;
        this.verifyService = verifyService;
        this.routeService = routeService;
        this.roleService = roleService;
        this.webSocketService = webSocketService;
    }

    public List<RegularUserDTO> getAll() {
        List<RegularUser> regularUsers = regularUserRepository.findAll();

        List<RegularUserDTO> regularUserDTOs = fromRegularUsers(regularUsers);
        regularUserDTOs.forEach(user ->  user.setProfilePicture(convertPictureToBase64ByName(user.getProfilePicture())));

        return regularUserDTOs;
    }

    public RegularUserDTO get(Long id) throws EntityNotFoundException {
        Optional<RegularUser> optionalRegularUser = regularUserRepository.findById(id);

        return optionalRegularUser.map(RegularUserDTO::new)
            .orElseThrow(() ->  new EntityNotFoundException(id, EntityType.USER));
    }

    public RegularUser getRegularById(Long id) throws EntityNotFoundException {

        return regularUserRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.USER));
    }

    public RegularUser getRegularByEmail(String email) throws EntityNotFoundException {
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
    ) throws MailCannotBeSentException, EntityAlreadyExistsException, EntityNotFoundException {
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

            return new RegistrationDTO(verifyDTO.getId(), email);
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException(ROLE_REGULAR_USER, EntityType.ROLE);
        } catch (Exception e) {
            throw new EntityAlreadyExistsException(String.format("User with %s already exists.", email));
        }
    }

    public boolean addToFavouriteRoutes(Long userId, Long routeId) throws EntityNotFoundException {
        RegularUser user = getRegularById(userId);
        Route route  = routeService.get(routeId);
        List<Route> favouriteRoutes = user.getFavouriteRoutes();
        favouriteRoutes.add(route);
        user.setFavouriteRoutes(favouriteRoutes);
        regularUserRepository.save(user);
        return true;
    }

    public boolean removeFromFavouriteRoutes(Long userId, Long routeId) throws EntityNotFoundException {
        RegularUser user = getRegularById(userId);
        System.out.println("routeeee" + routeId);
        List<Route> favouriteRoutes = user.getFavouriteRoutes();
        for(Route r : favouriteRoutes){
            if(r.getId().equals(routeId)){
                System.out.println("toooooo");
                favouriteRoutes.remove(r);
                break;
            }
        }
        System.out.println("routeeeee" + favouriteRoutes.size());
        user.setFavouriteRoutes(favouriteRoutes);
        regularUserRepository.save(user);
        return true;
    }

    public boolean isFavouriteRoute(Long routeId, Long userId) {
        RegularUser u = regularUserRepository.getUserWithFavouriteRouteId(routeId, userId);
        return u != null;
    }

    public List<RouteDTO> getFavouriteRoutes(Long id) throws EntityNotFoundException {
        Optional<RegularUser> u = regularUserRepository.findById(id);
        if(u.isPresent()){
            return fromRoutes(u.get().getFavouriteRoutes());
        }
        throw new EntityNotFoundException(id, EntityType.USER);
    }

    public boolean blockRegular(final Long id, String reason)
            throws EntityNotFoundException, EntityUpdateException
    {
        RegularUser regularUser = getRegularById(id);
        if (regularUserInActiveDriving(regularUser.getDrivings())) {
            throw new EntityUpdateException("Regular user cannot be blocked while in active driving.");
        }

        regularUser.setOnline(false);
        regularUser.setBlocked(true);
        regularUserRepository.save(regularUser);
        this.webSocketService.sendBlockedNotification(regularUser.getEmail(), reason);

        return true;
    }

    public boolean getIsBlocked(Long id) {

        return regularUserRepository.getIsBlocked(id);
    }

    public boolean unblock(Long id)
            throws EntityNotFoundException, EntityUpdateException {
        RegularUser regularUser = getRegularById(id);
        if (!regularUser.isBlocked()) {
            throw new EntityUpdateException(UNBLOCK_UNBLOCKED_USER_MESSAGE);
        }
        regularUser.setBlocked(false);
        regularUserRepository.save(regularUser);

        return true;
    }

    private boolean regularUserInActiveDriving(final List<Driving> drivings) {
        for (Driving driving : drivings) {
            if (driving.isActive()) {
                return true;
            }
        }

        return false;
    }

}
