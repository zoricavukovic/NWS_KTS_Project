package com.example.serbUber.service;

import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.dto.VehicleCurrentLocationForLocustDTO;
import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.*;
import com.example.serbUber.model.user.Driver;
import com.example.serbUber.service.interfaces.IVehicleService;
import com.example.serbUber.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.dto.VehicleCurrentLocationDTO.fromVehiclesToVehicleCurrentLocationDTO;
import static com.example.serbUber.dto.VehicleCurrentLocationForLocustDTO.fromVehiclesToVehicleCurrentLocationForLocustDTO;
import static com.example.serbUber.dto.VehicleDTO.fromVehicles;
import static com.example.serbUber.util.Constants.START_RATE;
import static com.example.serbUber.util.Constants.TAXI_START_LOCATION_ID;

@Component
@Qualifier("vehicleServiceConfiguration")
public class VehicleService implements IVehicleService {

    private VehicleRepository vehicleRepository;
    private VehicleTypeInfoService vehicleTypeInfoService;
    private WebSocketService webSocketService;
    private LocationService locationService;

    @Autowired
    public VehicleService(
            final VehicleRepository vehicleRepository,
            final VehicleTypeInfoService vehicleTypeInfoService,
            final WebSocketService webSocketService,
            final LocationService locationService
    ) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleTypeInfoService = vehicleTypeInfoService;
        this.webSocketService = webSocketService;
        this.locationService = locationService;
    }

    public Vehicle create(
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleType vehicleType
    ) throws EntityNotFoundException {
        Location stopLocation = locationService.get(TAXI_START_LOCATION_ID);

        return vehicleRepository.save(new Vehicle(
                petFriendly,
                babySeat,
                vehicleTypeInfoService.get(vehicleType),
                START_RATE,
                stopLocation
        ));
    }

    public List<VehicleDTO> getAll() {
        List<Vehicle> vehicles = vehicleRepository.findAll();

        return fromVehicles(vehicles);
    }

    public Vehicle getVehicleById(Long id) throws EntityNotFoundException {

        return vehicleRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException(id, EntityType.VEHICLE));
    }

    public double getRatingForVehicle(Long id) {

        return vehicleRepository.getVehicleRatingById(id);
    }

    public Vehicle updateRate(Long id, double rate) throws EntityNotFoundException {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setRate(rate);
        return vehicleRepository.save(vehicle);
    }

    public void delete(Long id) {

        vehicleRepository.deleteById(id);
    }

    public List<VehicleCurrentLocationDTO> getAllVehiclesForActiveDriver() throws EntityNotFoundException {

        List<Vehicle> vehicles = vehicleRepository.getAllVehiclesForActiveDriver();
        List<VehicleWithDriverId> withDriverIds = new LinkedList<>();
        for (Vehicle vehicle : vehicles) {
            Driver driver = getDriverByVehicleId(vehicle.getId());
            withDriverIds.add(new VehicleWithDriverId(vehicle, driver.getId(), driver.isActive()));
        }

        return fromVehiclesToVehicleCurrentLocationDTO(withDriverIds);
    }

    public Driver getDriverByVehicleId(final Long vehicleId) throws EntityNotFoundException {

        return vehicleRepository.getDriverByVehicleId(vehicleId)
            .orElseThrow(() -> new EntityNotFoundException(vehicleId, EntityType.VEHICLE));
    }

    public List<VehicleCurrentLocationForLocustDTO> getAllVehicleCurrentLocationForLocustDTO()
        throws EntityNotFoundException
    {
        List<Vehicle> vehicles = vehicleRepository.getAllVehicles();
        List<VehicleWithDriverId> withDriverIds = new LinkedList<>();

        for (Vehicle vehicle : vehicles) {
            Driver driver = getDriverByVehicleId(vehicle.getId());
            withDriverIds.add(new VehicleWithDriverId(vehicle, driver.getId(), driver.isActive()));
        }

        return fromVehiclesToVehicleCurrentLocationForLocustDTO(withDriverIds);
    }

    public VehicleDTO getVehicleOfDriver(final Long driverId) throws EntityNotFoundException {

        return new VehicleDTO(vehicleRepository.getVehicleByDriverId(driverId)
                .orElseThrow(() -> new EntityNotFoundException(driverId, EntityType.VEHICLE)));
    }

    public VehicleCurrentLocationForLocustDTO updateCurrentPosition(
        final Long id,
        final double lng,
        final double lat,
        final int crossedWaypoints,
        final int chosenRouteIdx
    ) throws EntityNotFoundException {
        Vehicle vehicle = getVehicleById(id);
        Location location = updateCurrentStopLocation(lng, lat, vehicle);
        vehicle.setCurrentStop(location);
        setLocationToExist(lng, lat, vehicle);
        vehicle.setCrossedWaypoints(crossedWaypoints);
        vehicle.setCurrentLocationIndex(vehicle.getCurrentLocationIndex() + 1);
        Driver driver = getDriverByVehicleId(vehicle.getId());
        vehicleRepository.save(vehicle);
        VehicleWithDriverId vehicleWithDriverId = new VehicleWithDriverId(vehicle, driver.getId(), driver.isActive());
        webSocketService.sendVehicleCurrentLocation(new VehicleCurrentLocationDTO(vehicleWithDriverId,chosenRouteIdx));

        return new VehicleCurrentLocationForLocustDTO(vehicleWithDriverId);
    }

    public VehicleCurrentLocationForLocustDTO checkStateOfVehicle(final Long id, final int chosenRouteIdx) throws EntityNotFoundException {
        Vehicle vehicle = getVehicleById(id);
        Driver driver = getDriverByVehicleId(vehicle.getId());
        VehicleWithDriverId vehicleWithDriverId = new VehicleWithDriverId(vehicle, driver.getId(), driver.isActive());
        webSocketService.sendVehicleCurrentLocation(new VehicleCurrentLocationDTO(vehicleWithDriverId, chosenRouteIdx));

        return new VehicleCurrentLocationForLocustDTO(vehicleWithDriverId);
    }

    public VehicleTypeInfo driverUpdateApprovalVehicle(final VehicleType vehicleType) throws EntityNotFoundException {

        return this.vehicleTypeInfoService.get(vehicleType);
    }

    private Location updateCurrentStopLocation(double lng, double lat, Vehicle vehicle) {
        Location location = vehicle.getCurrentStop();
        location.setLat(lat);
        location.setLon(lng);
        return location;
    }

    private void setLocationToExist(final double lng, final double lat, final Vehicle vehicle) {
        if (vehicle.getCurrentLocationIndex() == 0){
            vehicle.setCurrentStop(new Location(lat, lng));
        }
    }
}
