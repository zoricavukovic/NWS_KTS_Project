import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {BehaviorSubject, Observable} from 'rxjs';
import {GenericService} from "../generic-service/generic.service";
import {ConfigService} from "../config-service/config.service";
import {Driving} from "../../models/driving/driving";
import {VehicleCurrentLocation} from "../../models/vehicle/vehicle-current-location";
import {SimpleDrivingInfo} from "../../models/driving/simple-driving-info";
import { identifierName } from '@angular/compiler';

@Injectable({
  providedIn: 'root',
})
export class DrivingService extends GenericService<Driving> {

  ride$ = new BehaviorSubject<VehicleCurrentLocation>(null);

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    super(http, configService.DRIVINGS_URL);
  }

  getDrivingsForUser(
    id: number,
    pageNumber: number,
    pageSize: number,
    selectedSortBy: string,
    selectedSortOrder: string
  ) {
    return this.http.get(
      this.configService.drivingsSortPaginationUrl(id, pageNumber
        , pageSize, selectedSortBy, selectedSortOrder)
    );
  }

  getDrivingsForDriver(driverId: number): Observable<Driving[]> {

    return this.http.get<Driving[]>(this.configService.nowAndFutureDrivingsUrl(driverId));
  }

  // getCountDrivings(id: number) {
  //   return this.http.get<number>(this.configService);
  // }

  rejectDriving(drivingId: number, reason: string): Observable<Driving> {

    return this.http.put<Driving>(
      this.configService.rejectDrivingUrl(drivingId),
      reason
    );
  }

  finishDriving(drivingId: number): Observable<Driving> {

    return this.http.put<Driving>(this.configService.finishDrivingUrl(drivingId), null);
  }

  startDriving(drivingId: number):Observable<Driving> {

    return this.http.put<Driving>(this.configService.startDrivingUrl(drivingId), null);
  }

  checkIfUserHasActiveDriving(id: number): Observable<SimpleDrivingInfo> {

    return this.http.get<SimpleDrivingInfo>(this.configService.hasUserActiveDriving(id));
  }

  getVehicleDetails(drivingId: number): BehaviorSubject<VehicleCurrentLocation> {
    this.http
      .get<VehicleCurrentLocation>(this.configService.vehicleCurrentLocation(drivingId))
      .subscribe(vehiclesCurrentLocation => {
        this.ride$.next(vehiclesCurrentLocation);
      });

    return this.ride$;
  }

  updateRide(vehicleCurrentLocation: VehicleCurrentLocation) {
    this.ride$.next(vehicleCurrentLocation);
  }
}
