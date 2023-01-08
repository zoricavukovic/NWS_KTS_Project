import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { VehicleTypeInfo } from '../model/vehicle/vehicle-type-info';
import { GenericService } from './generic.service';

@Injectable({
  providedIn: 'root',
})
export class VehicleTypeInfoService extends GenericService<VehicleTypeInfo> {
  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    super(http, `${configService.api_url}/vehicle-type-infos`);
  }
}
