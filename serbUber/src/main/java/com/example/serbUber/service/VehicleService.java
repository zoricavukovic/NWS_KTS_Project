package com.example.serbUber.service;

import com.example.serbUber.dto.VehicleCurrentLocationDTO;
import com.example.serbUber.dto.VehicleDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.exception.EntityType;
import com.example.serbUber.service.interfaces.IVehicleService;
import com.example.serbUber.util.Constants;
import com.example.serbUber.model.Vehicle;
import com.example.serbUber.model.VehicleType;
import com.example.serbUber.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static com.example.serbUber.dto.VehicleCurrentLocationDTO.fromVehiclesToVehicleCurrentLocationDTO;
import static com.example.serbUber.dto.VehicleDTO.fromVehicles;

@Component
@Qualifier("vehicleServiceConfiguration")
public class VehicleService implements IVehicleService {

    private final VehicleRepository vehicleRepository;
    private final VehicleTypeInfoService vehicleTypeInfoService;
    private final WebSocketService webSocketService;
    private final RouteService routeService;

    public VehicleService(
            final VehicleRepository vehicleRepository,
            final VehicleTypeInfoService vehicleTypeInfoService,
            final WebSocketService webSocketService,
            final RouteService routeService
    ) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleTypeInfoService = vehicleTypeInfoService;
        this.webSocketService = webSocketService;
        this.routeService = routeService;
    }

    public Vehicle create(
            final boolean petFriendly,
            final boolean babySeat,
            final VehicleType vehicleType
    ) throws EntityNotFoundException {

        return vehicleRepository.save(new Vehicle(
                petFriendly,
                babySeat,
                vehicleTypeInfoService.get(vehicleType),
                Constants.STARTING_RATE
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

        List<List<double[]>> listOfVehiclesRoutes = new LinkedList<>();
        for (Vehicle vehicle : vehicles) {
            List<double[]> coordinatesList = routeService.getRoutePath(vehicle.getActiveRoute().getId());
            listOfVehiclesRoutes.add(coordinatesList);
        }

        return fromVehiclesToVehicleCurrentLocationDTO(vehicles, listOfVehiclesRoutes);
    }

    public List<VehicleCurrentLocationDTO> updateCurrentVehiclesLocation() throws EntityNotFoundException {
        List<Vehicle> vehicles = vehicleRepository.getAllVehiclesForActiveDriver();
        List<List<double[]>> listOfVehiclesRoutes = new LinkedList<>();
        for (Vehicle vehicle : vehicles) {
            List<double[]> coordinatesList = routeService.getRoutePath(vehicle.getActiveRoute().getId());
            listOfVehiclesRoutes.add(coordinatesList);
            saveCurrentVehicleLocation(vehicle, coordinatesList);
        }
        List<VehicleCurrentLocationDTO> vehicleCurrentLocationDTOs = fromVehiclesToVehicleCurrentLocationDTO(vehicles, listOfVehiclesRoutes);
        webSocketService.sendVehicleCurrentLocation(vehicleCurrentLocationDTOs);

        return vehicleCurrentLocationDTOs;
    }

    private void saveCurrentVehicleLocation(Vehicle vehicle, List<double[]> vehicleRoutePath) throws EntityNotFoundException {
        int currentLocationIndex = vehicle.getCurrentLocationIndex();

        int nextLocationIndex = (vehicleRoutePath.size() - 1 == currentLocationIndex)?
            currentLocationIndex :
            currentLocationIndex + 1;
        vehicle.setCurrentLocationIndex(nextLocationIndex);
        vehicleRepository.save(vehicle);
    }

    public Vehicle getVehicleByType(String vehicleType){
        VehicleType type = VehicleType.getVehicleType(vehicleType.toLowerCase());
        return vehicleRepository.getVehicleByType(type);
    }

    public VehicleDTO getVehicleDTOByVehicleType(String vehicleType){
        return new VehicleDTO(getVehicleByType(vehicleType));
    }
}
