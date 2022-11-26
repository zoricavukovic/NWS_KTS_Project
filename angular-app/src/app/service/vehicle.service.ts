import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { VehicleTypeInfo } from '../model/vehicle/vehicle-type-info';
import {BehaviorSubject, Observable} from 'rxjs';
import {Vehicle} from "../model/vehicle/vehicle";
import {ChatRoom} from "../model/message/chat-room";
import {ChatRoomWithNotify} from "../model/message/chat-room-with-notify";
import {VehicleCurrentLocation} from "../model/vehicle/vehicle-current-location";

@Injectable({
  providedIn: 'root',
})
export class VehicleService {

  vehicles$ = new BehaviorSubject<VehicleCurrentLocation[]>([]);
  constructor(private http: HttpClient, private configService: ConfigService) {}

  getVehicleTypeInfos(): Observable<VehicleTypeInfo[]> {
    return this.http.get<VehicleTypeInfo[]>(
      this.configService.vehicle_type_infos,
      { headers: this.configService.getHeader() }
    );
  }

  getAllActiveVehicles(): Observable<VehicleCurrentLocation[]> {
    return this.http.get<VehicleCurrentLocation[]>(
      this.configService.all_active_vehicles_url
    );
  }

  getAllVehicle(): BehaviorSubject<VehicleCurrentLocation[]> {
    this.http.get<VehicleCurrentLocation[]>(
      this.configService.all_active_vehicles_url
    ).subscribe(vehiclesCurrentLocation => {
      this.vehicles$.next(vehiclesCurrentLocation);
    });

    return this.vehicles$;
  }

  addVehicle(vehiclesCurrentLocation: VehicleCurrentLocation[]): void {

    this.vehicles$.next(vehiclesCurrentLocation);
  }
}
