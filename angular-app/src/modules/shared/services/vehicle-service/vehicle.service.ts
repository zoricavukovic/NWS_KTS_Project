import { HttpClient } from '@angular/common/http';
import {Injectable} from '@angular/core';
import {Observable} from 'rxjs';
import {Vehicle} from "../../models/vehicle/vehicle";
import {ConfigService} from "../config-service/config.service";
import {GenericService} from "../generic-service/generic.service";
import {VehicleCurrentLocation} from "../../models/vehicle/vehicle-current-location";

@Injectable({
  providedIn: 'root',
})
export class  VehicleService extends GenericService<Vehicle> {

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    super(http, configService.VEHICLES_URL);
  }

  getVehicleByDriver(driverId: string): Observable<Vehicle> {

    return this.http.get<Vehicle>(this.configService.vehicleByDriverId(driverId));
  }

  getAllVehicle(): Observable<VehicleCurrentLocation[]> {

    return this.http.get<VehicleCurrentLocation[]>(this.configService.ACTIVE_VEHICLES_URL);
  }
}
