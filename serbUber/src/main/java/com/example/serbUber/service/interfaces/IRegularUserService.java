package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.dto.user.RegistrationDTO;
import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.dto.user.UserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.user.RegularUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IRegularUserService {
    List<RegularUserDTO> getAll();
    RegularUserDTO get(Long id) throws EntityNotFoundException;
    RegularUser getRegularById(Long id) throws EntityNotFoundException;
    RegistrationDTO create(
            final String email,
            final String password,
            final String confirmationPassword,
            final String name,
            final String surname,
            final String phoneNumber,
            final String city,
            final String profilePicture
    ) throws PasswordsDoNotMatchException, EntityAlreadyExistsException, MailCannotBeSentException, EntityNotFoundException;
    boolean addToFavouriteRoutes(Long userId, Long routeId) throws EntityNotFoundException;
    boolean removeFromFavouriteRoutes(Long userId, Long routeId) throws EntityNotFoundException;
    boolean isFavouriteRoute(Long routeId, Long userId);
    List<RouteDTO> getFavouriteRoutes(Long id) throws EntityNotFoundException;
}
