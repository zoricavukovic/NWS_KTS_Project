package com.example.serbUber.service;

import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.dto.VehicleCurrentLocationForLocustDTO;
import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.model.*;
import com.example.serbUber.service.interfaces.IVehicleService;
import com.example.serbUber.util.Constants;
import com.example.serbUber.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.dto.VehicleCurrentLocationDTO.fromVehiclesToVehicleCurrentLocationDTO;
import static com.example.serbUber.dto.VehicleCurrentLocationForLocustDTO.fromVehiclesToVehicleCurrentLocationForLocustDTO;
import static com.example.serbUber.dto.VehicleDTO.fromVehicles;
import static com.example.serbUber.util.Constants.TAXI_START_LOCATION_ID;

@Component
@Qualifier("vehicleServiceConfiguration")
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleTypeInfoService vehicleTypeInfoService;
    private final WebSocketService webSocketService;
    private final RouteService routeService;
    private final LocationService locationService;

    public VehicleService(
            final VehicleRepository vehicleRepository,
            final VehicleTypeInfoService vehicleTypeInfoService,
            final WebSocketService webSocketService,
            final RouteService routeService,
            final LocationService locationService
    ) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleTypeInfoService = vehicleTypeInfoService;
        this.webSocketService = webSocketService;
        this.routeService = routeService;
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
                Constants.STARTING_RATE,
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
        List<List<double[]>> listOfVehiclesRoutes = new LinkedList<>();
        for (Vehicle vehicle : vehicles) {
            if (vehicle.hasRoute()){
                List<double[]> coordinatesList = routeService.getRoutePath(vehicle.getActiveRoute().getId());
                listOfVehiclesRoutes.add(coordinatesList);
            }
            else {
                List<double[]> coordinatesList = List.of(new double[]{vehicle.getCurrentStop().getLon(), vehicle.getCurrentStop().getLat()});
                listOfVehiclesRoutes.add(coordinatesList);
            }

            withDriverIds.add(new VehicleWithDriverId(vehicle, getDriverIdByVehicleId(vehicle.getId())));
        }

        return fromVehiclesToVehicleCurrentLocationDTO(withDriverIds, listOfVehiclesRoutes);
    }

    private Long getDriverIdByVehicleId(final Long vehicleId) throws EntityNotFoundException {

        return vehicleRepository.getDriverIdByVehicleId(vehicleId)
            .orElseThrow(() -> new EntityNotFoundException(vehicleId, EntityType.VEHICLE));
    }

    public List<VehicleCurrentLocationDTO> updateCurrentVehiclesLocation() throws EntityNotFoundException {
        List<Vehicle> vehicles = vehicleRepository.getAllVehiclesForActiveDriver();
        List<VehicleWithDriverId> withDriverIds = new LinkedList<>();
        List<List<double[]>> listOfVehiclesRoutes = new LinkedList<>();
        for (Vehicle vehicle : vehicles) {
            List<double[]> coordinatesList;
            if (vehicle.hasRoute()){
                coordinatesList = routeService.getRoutePath(vehicle.getActiveRoute().getId());
            }
            else {
                coordinatesList = List.of(new double[]{vehicle.getCurrentStop().getLon(), vehicle.getCurrentStop().getLat()});
            }
            listOfVehiclesRoutes.add(coordinatesList);
            saveCurrentVehicleLocation(vehicle, coordinatesList);
            withDriverIds.add(new VehicleWithDriverId(vehicle, getDriverIdByVehicleId(vehicle.getId())));
        }
        List<VehicleCurrentLocationDTO> vehicleCurrentLocationDTOs = fromVehiclesToVehicleCurrentLocationDTO(withDriverIds, listOfVehiclesRoutes);
        webSocketService.sendVehicleCurrentLocation(vehicleCurrentLocationDTOs);

        return vehicleCurrentLocationDTOs;
    }

    private void saveCurrentVehicleLocation(Vehicle vehicle, List<double[]> vehicleRoutePath) {
        int currentLocationIndex = vehicle.getCurrentLocationIndex();

        int nextLocationIndex = (vehicleRoutePath.size() - 1 == currentLocationIndex)?
            currentLocationIndex :
            currentLocationIndex + 1;
        vehicle.setCurrentLocationIndex(nextLocationIndex);
        if (vehicle.hasRoute()){
            vehicle.setCurrentStop(vehicle.getActiveRoute().getLocations().last().getLocation());
        }

        vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicleByType(String vehicleType){
        VehicleType type = VehicleType.getVehicleType(vehicleType.toLowerCase());
        return vehicleRepository.getVehicleByType(type);
    }

    public VehicleDTO getVehicleDTOByVehicleType(String vehicleType){
        return new VehicleDTO(getVehicleByType(vehicleType));
    }

    public double getLatOfCurrentVehiclePosition(final Vehicle vehicle) throws EntityNotFoundException {
        List<double[]> coordinatesList = routeService.getRoutePath(vehicle.getActiveRoute().getId());
        return (coordinatesList.size() >= vehicle.getCurrentLocationIndex())?
            coordinatesList.get(vehicle.getCurrentLocationIndex())[1]:
            -1;

    }

    public double getLonOfCurrentVehiclePosition(final Vehicle vehicle) throws EntityNotFoundException {
        List<double[]> coordinatesList = routeService.getRoutePath(vehicle.getActiveRoute().getId());
        return (coordinatesList.size() >= vehicle.getCurrentLocationIndex())?
            coordinatesList.get(vehicle.getCurrentLocationIndex())[0]:
            -1;

    }

    public List<VehicleCurrentLocationForLocustDTO> getAllVehicleCurrentLocationForLocustDTOForActiveDriver()
        throws EntityNotFoundException
    {
        List<Vehicle> vehicles = vehicleRepository.getAllVehiclesForActiveDriver();
        List<VehicleWithDriverId> withDriverIds = new LinkedList<>();

        for (Vehicle vehicle : vehicles) {
            withDriverIds.add(new VehicleWithDriverId(vehicle, getDriverIdByVehicleId(vehicle.getId())));
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
        final double lat
    ) throws EntityNotFoundException {
        Vehicle vehicle = getVehicleById(id);
        vehicle.setCurrentLocationIndex(vehicle.getCurrentLocationIndex() + 1);
        vehicle.setCurrentStop(new Location(lat, lng));
        return null;
    }

    public VehicleTypeInfo driverUpdateApprovalVehicle(final VehicleType vehicleType) throws EntityNotFoundException {

        return this.vehicleTypeInfoService.get(vehicleType);
    }
}
