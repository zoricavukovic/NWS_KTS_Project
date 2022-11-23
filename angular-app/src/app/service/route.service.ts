import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Observable } from 'rxjs';
import { VehicleTypeInfo } from '../model/vehicle/vehicle-type-info';
import { LocationsForRoutesRequest } from '../model/route/locations-for-routes-request';
import { PossibleRoute } from '../model/route/possible-routes';
import { map } from 'rxjs/operators';
import { PossibleRoutesViaPoints } from '../model/route/possible-routes-via-points';
import { Location } from '../model/route/location';

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
}
