import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Observable } from 'rxjs';
import { LocationsForRoutesRequest } from '../model/route/locations-for-routes-request';
import { PossibleRoutesViaPoints } from '../model/route/possible-routes-via-points';
import {LngLat, Location} from '../model/route/location';


@Injectable({
  providedIn: 'root',
})
export class RouteService {
  constructor(private http: HttpClient, private configService: ConfigService) {}

  getPossibleRoutes(
    locationsForCreateRoutes: LocationsForRoutesRequest
  ): Observable<PossibleRoutesViaPoints[]> {
    return this.http.post<PossibleRoutesViaPoints[]>(
      this.configService.option_routes,
      locationsForCreateRoutes
    );
  }

  createLocationForRoutesRequest(
    locationsForRouteRequest: Location[]
  ): LocationsForRoutesRequest {
    return {
      locationsForRouteRequest: locationsForRouteRequest,
    };
  }

  getRoutePath(routeId: number): Observable<LngLat[]> {

    return this.http.get<LngLat[]>(this.configService.routePathUrl(routeId));
  }
}
