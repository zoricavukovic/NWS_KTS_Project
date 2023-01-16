package com.example.serbUber.controller.user;

import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.dto.chart.ChartItemDTO;
import com.example.serbUber.dto.user.RegularUserDTO;
import com.example.serbUber.dto.user.RegularUserPageDTO;
import com.example.serbUber.exception.*;
import com.example.serbUber.request.user.FavouriteRouteRequest;
import com.example.serbUber.service.user.RegularUserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

import static com.example.serbUber.exception.ErrorMessagesConstants.*;

@RestController
@RequestMapping("/regular-users")
public class RegularUserController {
    private final RegularUserService regularUserService;

    public RegularUserController(@Qualifier("regularUserServiceConfiguration") final RegularUserService regularUserService) {
        this.regularUserService = regularUserService;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<RegularUserDTO> getAll() {

        return regularUserService.getAll();
    }

    @PostMapping("/favourite")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public boolean updateFavouriteRoutes(@RequestBody FavouriteRouteRequest request) throws EntityNotFoundException {

        return regularUserService.updateFavouriteRoutes(request.getUserId(), request.getRouteId());
    }

    @GetMapping("/blocked-data/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public boolean getBlocked(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id
    ) throws EntityNotFoundException {

        return regularUserService.getIsBlocked(id);
    }

    @GetMapping("/favourite-route/{routeId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_DRIVER', 'ROLE_REGULAR_USER')")
    public boolean isFavouriteRoute(@Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long routeId, @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long userId){
        return regularUserService.isFavouriteRoute(routeId, userId);
    }

    @GetMapping("/favourite-routes/{id}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_REGULAR_USER')")
    public List<RouteDTO> getFavouriteRoutes(@Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id) throws EntityNotFoundException {
        return regularUserService.getFavouriteRoutes(id);
    }

    @PutMapping("/unblock/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public boolean unblock(
            @Valid @NotNull(message = NOT_NULL_MESSAGE) @PathVariable Long id
    ) throws EntityNotFoundException, EntityUpdateException {

        return regularUserService.unblock(id);
    }

    @GetMapping("/{pageNumber}/{pageSize}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public List<RegularUserPageDTO> getDriversWithPagination(@Valid @NotNull(message = NOT_NULL_MESSAGE) @PositiveOrZero(message = POSITIVE_OR_ZERO_MESSAGE) @PathVariable int pageNumber,
                                                             @Valid @NotNull(message = NOT_NULL_MESSAGE) @Positive(message = POSITIVE_MESSAGE) @PathVariable int pageSize){
        return regularUserService.getRegularUsersWithPagination(pageNumber, pageSize);
    }

}
