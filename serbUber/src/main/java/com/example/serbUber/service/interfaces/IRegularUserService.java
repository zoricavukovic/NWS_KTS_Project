package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.dto.user.RegistrationDTO;
import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.model.user.RegularUser;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IRegularUserService {
    List<RegularUserDTO> getAll();
    RegularUserDTO get(final Long id) throws EntityNotFoundException;
    RegularUser getRegularById(final Long id) throws EntityNotFoundException;
    boolean addToFavouriteRoutes(final Long userId, final Long routeId) throws EntityNotFoundException;
    boolean removeFromFavouriteRoutes(final Long userId, final Long routeId) throws EntityNotFoundException;
    boolean isFavouriteRoute(final Long routeId, final Long userId);
    List<RouteDTO> getFavouriteRoutes(final Long id) throws EntityNotFoundException;
    public RegistrationDTO registerRegularUser(
        final String email,
        final String password,
        final String name,
        final String surname,
        final String phoneNumber,
        final String city,
        final String profilePicture
    ) throws MailCannotBeSentException, EntityAlreadyExistsException, EntityNotFoundException;
}
