package com.example.serbUber.service;

import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.Location;
import com.example.serbUber.repository.DrivingLocationIndexRepository;
import com.example.serbUber.service.interfaces.IDrivingLocationIndexService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("drivingLocationIndexServiceConfiguration")
public class DrivingLocationIndexService implements IDrivingLocationIndexService {

    private final DrivingLocationIndexRepository drivingLocationIndexRepository;

    public DrivingLocationIndexService(final DrivingLocationIndexRepository drivingLocationIndexRepository){
        this.drivingLocationIndexRepository = drivingLocationIndexRepository;
    }
    
    public DrivingLocationIndex create(final Location location, final int index, final int routeIndex){
        return drivingLocationIndexRepository.save(new DrivingLocationIndex(location, index, routeIndex));
    }
}
