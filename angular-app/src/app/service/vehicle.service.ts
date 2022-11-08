import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { VehicleTypeInfo } from '../model/response/vehicle-type-info';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class VehicleService {

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) { }

  getVehicleTypeInfos(): Observable<VehicleTypeInfo[]> {

    return this.http.get<VehicleTypeInfo[]>(this.configService.vehicle_type_infos)
  }

}
