package com.example.serbUber.service.user;

import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.dto.VerifyDTO;
import com.example.serbUber.dto.user.RegistrationDTO;
import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.Verify;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.user.RegularUserRepository;
import com.example.serbUber.service.RouteService;
import com.example.serbUber.service.VerifyService;
import com.example.serbUber.service.interfaces.IRegularUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.RouteDTO.fromRoutes;
import static com.example.serbUber.dto.user.RegularUserDTO.fromRegularUsers;
import static com.example.serbUber.model.user.User.passwordsDontMatch;
import static com.example.serbUber.util.Constants.ROLE_REGULAR_USER;
import static com.example.serbUber.util.Constants.getProfilePicture;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;

@Component
@Qualifier("regularUserServiceConfiguration")
public class RegularUserService implements IRegularUserService {

    private final RegularUserRepository regularUserRepository;
    private final VerifyService verifyService;
    private final RoleService roleService;
    private final RouteService routeService;
    private final UserService userService;

    public RegularUserService(
            final RegularUserRepository regularUserRepository,
            final VerifyService verifyService,
            final RouteService routeService,
            final RoleService roleService,
            final UserService userService
    ) {
        this.regularUserRepository = regularUserRepository;
        this.verifyService = verifyService;
        this.routeService = routeService;
        this.roleService = roleService;
        this.userService = userService;
    }

    public List<RegularUserDTO> getAll() {
        List<RegularUser> regularUsers = regularUserRepository.findAll();

        return fromRegularUsers(regularUsers);
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

    public RegistrationDTO create(
        final String email,
        final String password,
        final String confirmationPassword,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture
    ) throws PasswordsDoNotMatchException, EntityAlreadyExistsException, MailCannotBeSentException, EntityNotFoundException {
        if (passwordsDontMatch(password, confirmationPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if (userService.checkIfUserAlreadyExists(email)) {
            throw new EntityAlreadyExistsException(String.format("User with %s already exists.", email));
        }

        return registerRegularUser(email, password, name, surname, phoneNumber, city, profilePicture);
    }

    private RegistrationDTO registerRegularUser(
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

}
