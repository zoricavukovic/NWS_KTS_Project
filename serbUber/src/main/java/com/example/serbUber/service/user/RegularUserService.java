package com.example.serbUber.service.user;

import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.Verify;
import com.example.serbUber.model.user.RegularUser;
import com.example.serbUber.repository.user.RegularUserRepository;
import com.example.serbUber.service.RouteService;
import com.example.serbUber.service.VerifyService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.serbUber.dto.RouteDTO.fromRoutes;
import static com.example.serbUber.dto.user.RegularUserDTO.fromRegularUsers;
import static com.example.serbUber.model.user.User.passwordsDontMatch;
import static com.example.serbUber.util.Constants.ROLE_REGULAR_USER;
import static com.example.serbUber.util.Constants.getProfilePicture;
import static com.example.serbUber.util.JwtProperties.getHashedNewUserPassword;

@Service
public class RegularUserService {

    private final RegularUserRepository regularUserRepository;
    private final VerifyService verifyService;
    private final RoleService roleService;
    private final RouteService routeService;

    public RegularUserService(
            final RegularUserRepository regularUserRepository,
            final VerifyService verifyService,
            final RouteService routeService,
            final RoleService roleService
    ) {
        this.regularUserRepository = regularUserRepository;
        this.verifyService = verifyService;
        this.routeService = routeService;
        this.roleService = roleService;
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

    public RegularUserDTO create(
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
        RegularUser regularUser = saveRegularUser(email, password, name, surname, phoneNumber, city, profilePicture);

        return new RegularUserDTO(regularUser);
    }

    private RegularUser saveRegularUser(
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
            verifyService.sendEmail(regularUser.getId(), regularUser.getEmail());

            return regularUser;
        } catch (EntityNotFoundException ex) {
            throw new EntityNotFoundException(ROLE_REGULAR_USER, EntityType.ROLE);
        } catch (IllegalArgumentException e) {
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

    public UserDTO activate(final Long verifyId, final int securityCode)
            throws EntityNotFoundException, WrongVerifyTryException
    {
        Verify verify = verifyService.update(verifyId, securityCode);
        RegularUser regularUser = getRegularById(verify.getUserId());
        regularUser.setVerified(true);

        return new UserDTO(regularUserRepository.save(regularUser));
    }

}
