import { HttpClient } from '@angular/common/http';
import {Injectable} from '@angular/core';
import {BehaviorSubject, map, Observable, take} from 'rxjs';
import {Vehicle} from "../../models/vehicle/vehicle";
import {ConfigService} from "../config-service/config.service";
import {GenericService} from "../generic-service/generic.service";
import {VehicleCurrentLocation} from "../../models/vehicle/vehicle-current-location";
import {ChatRoom} from "../../models/message/chat-room";

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

  // updateVehiclePosition(vehicleCurrentLocation: VehicleCurrentLocation): void {
  //   console.log("PRE NEGO STO UPDATEUJE");
  //   // console.log(vehicleCurrentLocation);
  //   const vehicle: VehicleCurrentLocation = this.vehicles$.getValue().find(vehicle=> {
  //     return vehicle.id === vehicleCurrentLocation.id
  //   })
  //   // console.log(this.vehicles$);
  //   // console.log(vehicle);
  //   // console.log(vehiclesCurrentLocation);
  //   if (vehicle){
  //     const index: number = this.vehicles$.getValue().indexOf(vehicle);
  //     console.log(index);
  //     console.log(vehicleCurrentLocation.currentLocation);
  //
  //     this.vehicles$.value.at(index).currentLocation.lat = vehicleCurrentLocation.currentLocation.lat;
  //     this.vehicles$.value.at(index).currentLocation.lon = vehicleCurrentLocation.currentLocation.lon;
  //     // console.log(this.vehicles$);
  //     // // console.log("PREGLED");
  //     console.log(this.vehicles$.value.at(index));
  //
  //     this.vehicles$.next(this.vehicles$.value);
  //     console.log(this.vehicles$);
  //   } else {
  //     this.vehicles$.next([...this.vehicles$.getValue(), vehicle]);
  //   }
  // }




  getVehicleByVehicleType(vehicleType: string): Observable<Vehicle> {

    return this.http.get<Vehicle>(this.configService.vehicleByVehicleTypeUrl(vehicleType));
  }
}
