package com.example.serbUber.service;

import com.example.serbUber.dto.DrivingDTO;
import com.example.serbUber.dto.DrivingPageDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.Driving;
import com.example.serbUber.model.DrivingStatus;
import com.example.serbUber.model.Route;
import com.example.serbUber.model.user.User;
import com.example.serbUber.repository.DrivingRepository;
import com.example.serbUber.service.interfaces.IDrivingService;
import com.example.serbUber.service.user.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

import static com.example.serbUber.dto.DrivingDTO.fromDrivings;
import static com.example.serbUber.dto.DrivingPageDTO.fromDrivingsPage;

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
        LocalDateTime end = started.plusMinutes(duration);
        Driving driving = drivingRepository.save(
                new Driving(active, duration, started, end, payingLimit, route, drivingStatus, driverId, usersPaid, price)
        );

        return new DrivingDTO(driving);
    }

    public List<DrivingDTO> getAll() {
        List<Driving> drivings = drivingRepository.findAll();

        return fromDrivings(drivings);
    }

    public List<DrivingPageDTO> getDrivingsForUser(
            final Long id,
            final int pageNumber,
            final int pageSize,
            final String parameter,
            final String sortOrder
    ) throws EntityNotFoundException {
        Pageable page = PageRequest.of(pageNumber, pageSize, Sort.by(getSortOrder(sortOrder), getSortBy(parameter)));
        Page<Driving> results = getDrivingPage(id, page);
        //int numberOfPages = calculateTotalNumberOfPages(results.getTotalPages(), results.getSize());
        return fromDrivingsPage(results.getContent(), results.getSize(), results.getTotalPages());
    }

    private int calculateTotalNumberOfPages(final int totalNumber, final int pageSize){
        return totalNumber % pageSize == 0 ?
                totalNumber / pageSize :
                totalNumber / pageSize + 1;
    }


    private Page<Driving> getDrivingPage(Long id, Pageable page) throws EntityNotFoundException {
        User user = userService.getUserById(id);
        Page<Driving> drivings = drivingRepository.findByUserId(id, page);
        return user.getRole().isDriver() ?
                drivingRepository.findByDriverId(id, page) :
                drivingRepository.findByUserId(id, page);
    }


    private String getSortBy(final String sortBy) {
        Dictionary<String, String> sortByDict = new Hashtable<>();
        sortByDict.put("Date", "started");
        sortByDict.put("Departure", "route.startPoint");
        sortByDict.put("Destination", "route.locations"); //ne znam ovo kako da pristupi??
        sortByDict.put("Price", "price");

        return sortByDict.get(sortBy);
    }

    private Sort.Direction getSortOrder(final String sortOrder) {
        Dictionary<String, Sort.Direction> sortOrderDict = new Hashtable<>();
        sortOrderDict.put("Descending", Sort.Direction.DESC);
        sortOrderDict.put("Ascending", Sort.Direction.ASC);

        return sortOrderDict.get(sortOrder);
    }

    public DrivingDTO getDrivingDto(final Long id) throws EntityNotFoundException {

        return new DrivingDTO(getDriving(id));
    }

    public Driving getDriving(final Long id) throws EntityNotFoundException {

        return drivingRepository.getDrivingById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, EntityType.DRIVING));
    }

    public List<DrivingDTO> getAllNowAndFutureDrivings(final Long id) {

        return fromDrivings(drivingRepository.getAllNowAndFutureDrivings(id));
    }

    public DrivingDTO finishDriving(final Long id) throws EntityNotFoundException {
        Driving driving = getDriving(id);
        driving.setActive(false);
        drivingRepository.save(driving);

        return new DrivingDTO(driving);
    }

    public int getNumberOfAllDrivingsForUser(final Long id) throws EntityNotFoundException {
        User user = userService.getUserById(id);
        return user.getRole().isDriver() ?
                drivingRepository.getNumberOfAllDrivingsForDriver(id).size() :
                drivingRepository.getNumberOfAllDrivingsForRegularUser(id).size();
    }
}
