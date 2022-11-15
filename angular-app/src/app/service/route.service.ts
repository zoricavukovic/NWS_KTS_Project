import { Injectable } from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {ConfigService} from "./config.service";
import {Observable} from "rxjs";
import {VehicleTypeInfo} from "../model/response/vehicle-type-info";
import {LocationsForRoutesRequest} from "../model/request/locations-for-routes-request";
import {PossibleRoute} from "../model/response/possible-routes";
import {map} from "rxjs/operators";
import {PossibleRoutesViaPoints} from "../model/response/possible-routes-via-points";

@Injectable({
  providedIn: 'root'
})
export class RouteService {

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) { }

  getPossibleRoutes(locationsForCreateRoutes: LocationsForRoutesRequest): Observable<PossibleRoutesViaPoints[]>{

    return this.http.post<PossibleRoutesViaPoints[]>(this.configService.option_routes, locationsForCreateRoutes);
  }
}
