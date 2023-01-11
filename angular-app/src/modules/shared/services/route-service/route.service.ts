import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {LocationsForRoutesRequest} from "../../models/route/locations-for-routes-request";
import {ConfigService} from "../config-service/config.service";
import {PossibleRoutesViaPoints} from "../../models/route/possible-routes-via-points";
import {LngLat, Location} from "../../models/route/location";

@Injectable({
  providedIn: 'root',
})
export class RouteService {
  constructor(private http: HttpClient, private configService: ConfigService) {}

  getPossibleRoutes(
    locationsForCreateRoutes: LocationsForRoutesRequest
  ): Observable<PossibleRoutesViaPoints[]> {
    return this.http.post<PossibleRoutesViaPoints[]>(
      this.configService.POSSIBLE_ROUTES_URL,
      locationsForCreateRoutes
    );
  }

  createLocationForRoutesRequest(locationsForRouteRequest: Location[]): LocationsForRoutesRequest {
    return {
      locationsForRouteRequest: locationsForRouteRequest,
    };
  }

  getRoutePath(routeId: number): Observable<LngLat[]> {

    return this.http.get<LngLat[]>(this.configService.routePathUrl(routeId));
  }
}
