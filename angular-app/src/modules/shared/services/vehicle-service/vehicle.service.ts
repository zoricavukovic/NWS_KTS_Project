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
    super(http, `${configService.api_url}/vehicles`);
  }

  getPriceForVehicleAndRoute(type: string, kilometers: number) {
    console.log(this.configService.get_price_for_driving(type, kilometers));

    return this.http.get<number>(this.configService.get_price_for_driving(type, kilometers));
  }

  getAllActiveVehicles(): Observable<VehicleCurrentLocation[]> {
    return this.http.get<VehicleCurrentLocation[]>(
      this.configService.all_active_vehicles_url
    );
  }

  getAllVehicle(): BehaviorSubject<VehicleCurrentLocation[]> {
    this.http
      .get<VehicleCurrentLocation[]>(this.configService.all_active_vehicles_url)
      .subscribe(vehiclesCurrentLocation => {
        console.log("cao ja sam");
        console.log(vehiclesCurrentLocation);
        this.vehicles$.next(vehiclesCurrentLocation);
      });

    return this.vehicles$;
  }

  addVehicle(vehiclesCurrentLocation: VehicleCurrentLocation[]): void {
    this.vehicles$.next(vehiclesCurrentLocation);
  }

  getVehicleByVehicleType(vehicleType: string): Observable<Vehicle> {

    return this.http.get<Vehicle>(this.configService.get_vehicle_by_vehicle_type(vehicleType));
  }
}
