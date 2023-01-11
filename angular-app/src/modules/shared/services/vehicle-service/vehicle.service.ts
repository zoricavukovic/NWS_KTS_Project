import { HttpClient } from '@angular/common/http';
import {Injectable} from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import {Vehicle} from "../../models/vehicle/vehicle";
import {ConfigService} from "../config-service/config.service";
import {GenericService} from "../generic-service/generic.service";
import {VehicleCurrentLocation} from "../../models/vehicle/vehicle-current-location";

@Injectable({
  providedIn: 'root',
})
export class VehicleService extends GenericService<Vehicle> {
  vehicles$ = new BehaviorSubject<VehicleCurrentLocation[]>([]);
  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    super(http, configService.VEHICLES_URL);
  }

  getPriceForVehicleAndRoute(type: string, kilometers: number) {

    return this.http.get<number>(this.configService.priceForRouteAndVehicleUrl(type, kilometers));
  }

  getAllActiveVehicles(): Observable<VehicleCurrentLocation[]> {
    return this.http.get<VehicleCurrentLocation[]>(
      this.configService.ACTIVE_VEHICLES_URL
    );
  }

  getAllVehicle(): BehaviorSubject<VehicleCurrentLocation[]> {
    this.http
      .get<VehicleCurrentLocation[]>(this.configService.ACTIVE_VEHICLES_URL)
      .subscribe(vehiclesCurrentLocation => {
        this.vehicles$.next(vehiclesCurrentLocation);
      });

    return this.vehicles$;
  }

  addVehicle(vehiclesCurrentLocation: VehicleCurrentLocation[]): void {
    this.vehicles$.next(vehiclesCurrentLocation);
  }

  getVehicleByVehicleType(vehicleType: string): Observable<Vehicle> {

    return this.http.get<Vehicle>(this.configService.vehicleByVehicleTypeUrl(vehicleType));
  }
}
