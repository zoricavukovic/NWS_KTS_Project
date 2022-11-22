package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.DrivingRepository;
import com.example.serbUber.repository.user.UserRepository;
import com.example.serbUber.service.interfaces.IDrivingService;
import com.example.serbUber.service.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.example.serbUber.dto.DrivingDTO.fromDrivings;
import static com.example.serbUber.util.Constants.ROLE_DRIVER;

@Component
@Qualifier("drivingServiceConfiguration")
public class DrivingService implements IDrivingService {

    private final DrivingRepository drivingRepository;
    private final UserService userService;

    public DrivingService(final DrivingRepository drivingRepository, final UserService userService) {
        this.drivingRepository = drivingRepository;
        this.userService = userService;
    }

    public DrivingDTO create(
            final boolean active,
            final int duration,
            final LocalDateTime started,
            final LocalDateTime payingLimit,
            final Route route,
            final DrivingStatus drivingStatus,
            final Long driverId,
            final HashMap<Long, Boolean> usersPaid,
            final double price
    ) {

        Driving driving = drivingRepository.save(new Driving(
                active,
                duration,
                started,
                payingLimit,
                route,
                drivingStatus,
                driverId,
                usersPaid,
                price
        ));

        return new DrivingDTO(driving);
    }

    public List<DrivingDTO> getAll() {
        List<Driving> drivings = drivingRepository.findAll();

        return fromDrivings(drivings);
    }

    public List<DrivingDTO> getDrivingsForUser(Long id, int pageNumber, int pageSize, String parameter, String sortOrder) throws EntityNotFoundException {
        User user = userService.getUserById(id);
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by(getSortOrder(sortOrder), getSortBy(parameter)));
        /*List<Driving> drivings = drivingRepository.findByUserId(id, page);
        drivings.forEach(driving -> sortLocations(driving.getRoute().getLocations()));*/
        return user.getRole().isDriver() ?
                fromDrivings(drivingRepository.findByDriverId(id, page)) :
                fromDrivings(drivingRepository.findByUserId(id, page));
    }

    /*private SortedSet<DrivingLocationIndex> sortLocations(SortedSet<DrivingLocationIndex> locations){
        return locations.stream()
                .sorted(Comparator.comparingInt(DrivingLocationIndex::getIndex))
                .collect(Collectors.toSet());
    }*/

    private String getSortBy(String sortBy){
        Dictionary<String, String> sortByDict = new Hashtable<>();
        sortByDict.put("Date","started");
        sortByDict.put("Departure","route.startPoint");
        sortByDict.put("Destination","route.locations"); //ne znam ovo kako da pristupi??
        sortByDict.put("Price","price");

        return sortByDict.get(sortBy);
    }

    private Sort.Direction getSortOrder(String sortOrder){
        Dictionary<String, Sort.Direction> sortOrderDict = new Hashtable<>();
        sortOrderDict.put("Descending", Sort.Direction.DESC);
        sortOrderDict.put("Ascending", Sort.Direction.ASC);

        return sortOrderDict.get(sortOrder);
    }

    public DrivingDTO getDrivingDto(Long id) throws EntityNotFoundException {

        return new DrivingDTO(getDriving(id));
    }

    public Driving getDriving(Long id) throws EntityNotFoundException {

        return drivingRepository.getDrivingById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING));
    }

    public List<DrivingDTO> getAllNowAndFutureDrivings(Long id) {

        return fromDrivings(drivingRepository.getAllNowAndFutureDrivings(id));
    }
}
