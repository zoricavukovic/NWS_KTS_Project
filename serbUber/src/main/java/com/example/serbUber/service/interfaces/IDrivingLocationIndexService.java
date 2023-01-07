package com.example.serbUber.service.interfaces;

import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.Location;
import org.springframework.stereotype.Service;

@Service
public interface IDrivingLocationIndexService {

    DrivingLocationIndex create(final Location location, final int index, final int routeIndex);
}
