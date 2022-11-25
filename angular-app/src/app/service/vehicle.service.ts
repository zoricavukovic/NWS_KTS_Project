import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { VehicleTypeInfo } from '../model/vehicle/vehicle-type-info';
import { Observable } from 'rxjs';
import {Vehicle} from "../model/vehicle/vehicle";

@Injectable({
  providedIn: 'root',
})
export class VehicleService {
  constructor(private http: HttpClient, private configService: ConfigService) {}

  getVehicleTypeInfos(): Observable<VehicleTypeInfo[]> {
    return this.http.get<VehicleTypeInfo[]>(
      this.configService.vehicle_type_infos,
      { headers: this.configService.getHeader() }
    );
  }

  getAllActiveVehicles(): Observable<Vehicle[]> {
    return this.http.get<Vehicle[]>(
      this.configService.all_active_vehicles_url
    );
  }
}
