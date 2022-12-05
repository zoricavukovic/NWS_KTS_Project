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
    Route get(Long id) throws EntityNotFoundException;
    RouteDTO createDTO(
            final SortedSet<DrivingLocationIndex> locations,
            final double distance,
            final double time,
            final SortedSet<Integer> routePathIndex
    );

    Route create(
            final SortedSet<DrivingLocationIndex> locations,
            final double distance,
            final double time,
            final SortedSet<Integer> routePathIndex
    );
    List<PossibleRoutesViaPointsDTO> getPossibleRoutes(LocationsForRoutesRequest locationsForRouteRequest);

}
