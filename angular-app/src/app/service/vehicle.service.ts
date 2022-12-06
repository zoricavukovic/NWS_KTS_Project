import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';
import { VehicleTypeInfo } from '../model/vehicle/vehicle-type-info';
import { BehaviorSubject, Observable } from 'rxjs';
import { Vehicle } from '../model/vehicle/vehicle';
import { ChatRoom } from '../model/message/chat-room';
import { ChatRoomWithNotify } from '../model/message/chat-room-with-notify';
import { VehicleCurrentLocation } from '../model/vehicle/vehicle-current-location';
import { GenericService } from './generic.service';
import { HeadersService } from './headers.service';

@Injectable({
  providedIn: 'root',
})
export class VehicleService extends GenericService<Vehicle> {
  vehicles$ = new BehaviorSubject<VehicleCurrentLocation[]>([]);
  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private headersService: HeadersService
  ) {
    super(http, `${configService.api_url}/vehicles`, headersService);
  }

  getPriceForVehicleAndRoute(type: string, kilometers: number) {
    console.log(this.configService.get_price_for_driving(type, kilometers));
    return this.http.get<number>(
      this.configService.get_price_for_driving(type, kilometers),
      { headers: this.configService.getHeader() }
    );
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
        this.vehicles$.next(vehiclesCurrentLocation);
      });

    return this.vehicles$;
  }

  addVehicle(vehiclesCurrentLocation: VehicleCurrentLocation[]): void {
    this.vehicles$.next(vehiclesCurrentLocation);
  }

  getVehicleByVehicleType(vehicleType: string): Observable<Vehicle> {
    return this.http.get<Vehicle>(
      this.configService.get_vehicle_by_vehicle_type(vehicleType),
      { headers: this.configService.getHeader() }
    );
  }
}
