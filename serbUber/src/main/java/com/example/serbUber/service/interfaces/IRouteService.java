package com.example.serbUber.service.interfaces;

import com.example.serbUber.dto.PossibleRoutesViaPointsDTO;
import com.example.serbUber.dto.RouteDTO;
import com.example.serbUber.exception.EntityNotFoundException;
import com.example.serbUber.model.DrivingLocationIndex;
import com.example.serbUber.model.Route;
import com.example.serbUber.request.LocationsForRoutesRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.SortedSet;

@Service
public interface IRouteService {
    List<RouteDTO> getAll();
    Route get(final Long id) throws EntityNotFoundException;

    Route create(
            final SortedSet<DrivingLocationIndex> locations,
            final double distance,
            final double time
    );
    List<PossibleRoutesViaPointsDTO> getPossibleRoutes(
        final LocationsForRoutesRequest locationsForRouteRequest
    );

    List<double[]> getRoutePath(final Long id) throws EntityNotFoundException;
}
