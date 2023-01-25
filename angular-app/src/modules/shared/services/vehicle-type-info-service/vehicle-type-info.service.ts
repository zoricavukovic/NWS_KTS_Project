import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { GenericService } from '../generic-service/generic.service';
import { ConfigService } from '../config-service/config.service';
import { VehicleTypeInfo } from '../../models/vehicle/vehicle-type-info';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class VehicleTypeInfoService extends GenericService<VehicleTypeInfo> {
  constructor(private http: HttpClient, private configService: ConfigService) {
    super(http, configService.VEHICLE_TYPE_INFOS_URL);
  }

  getPriceForVehicleAndRoute(
    type: string,
    kilometers: number
  ): Observable<number> {
    return this.http.get<number>(
      this.configService.priceForRouteAndVehicleUrl(type, kilometers)
    );
  }

  getAveragePriceForRoute(kilometers: number): Observable<number> {
    return this.http.get<number>(
      this.configService.averagePriceForRoute(kilometers)
    );
  }
}
