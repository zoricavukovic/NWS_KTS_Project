import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {GenericService} from "../generic-service/generic.service";
import {ConfigService} from "../config-service/config.service";
import {VehicleTypeInfo} from "../../models/vehicle/vehicle-type-info";

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
