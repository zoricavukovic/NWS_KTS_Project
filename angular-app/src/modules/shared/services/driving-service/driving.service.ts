import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { GenericService } from '../generic-service/generic.service';
import { ConfigService } from '../config-service/config.service';
import { Driving } from '../../models/driving/driving';
import { VehicleCurrentLocation } from '../../models/vehicle/vehicle-current-location';
import { SimpleDrivingInfo } from '../../models/driving/simple-driving-info';
import { ChartData, ChartRequest } from '../../models/chart/chart-data';

@Injectable({
  providedIn: 'root',
})
export class DrivingService extends GenericService<Driving> {
  ride$ = new BehaviorSubject<VehicleCurrentLocation>(null);

  constructor(private http: HttpClient, private configService: ConfigService) {
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
      this.configService.drivingsSortPaginationUrl(
        id,
        pageNumber,
        pageSize,
        selectedSortBy,
        selectedSortOrder
      )
    );
  }

  getDrivingByFavouriteRoute(routeId: number): Observable<number> {
    return this.http.get<number>(
      this.configService.drivingByFavouriteRoute(routeId)
    );
  }

  getTimeForDriving(drivingId: number): Observable<Date> {
    return this.http.get<Date>(
      this.configService.getTimeForDrivingUrl(drivingId)
    );
  }

  getDrivingsForDriver(driverId: number): Observable<Driving[]> {
    return this.http.get<Driving[]>(
      this.configService.nowAndFutureDrivingsUrl(driverId)
    );
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
    return this.http.put<Driving>(
      this.configService.finishDrivingUrl(drivingId),
      null
    );
  }

  startDriving(drivingId: number): Observable<Driving> {
    return this.http.put<Driving>(
      this.configService.startDrivingUrl(drivingId),
      null
    );
  }

  checkIfUserHasActiveDriving(id: number): Observable<SimpleDrivingInfo> {
    return this.http.get<SimpleDrivingInfo>(
      this.configService.hasUserActiveDriving(id)
    );
  }

  updateRide(vehicleCurrentLocation: VehicleCurrentLocation) {
    this.ride$.next(vehicleCurrentLocation);
  }

  havePassengersAlreadyRide(
    passengers: string[],
    started: Date
  ): Observable<boolean> {
    return this.http.post<boolean>(
      this.configService.HAVE_PASSENGERS_ALREADY_RIDE_URL,
      { passengersEmail: passengers, started: started }
    );
  }

  getChartData(chartRequest: ChartRequest): Observable<ChartData> {
    return this.http.post<ChartData>(
      this.configService.getChartData(),
      chartRequest
    );
  }

  getAdminChartData(chartRequest: ChartRequest): Observable<ChartData> {
    return this.http.post<ChartData>(
      this.configService.getAdminChartData(),
      chartRequest
    );
  }

  getDrivingForUser(id: number, userId: number) {
    return this.http.get<Driving>(
      this.configService.getDrivingForUserUrl(id, userId)
    );
  }

  getDriving(id: number, userId: number, isRegularUser: boolean) {
    return isRegularUser ? this.getDrivingForUser(id, userId) : this.get(id);
  }

  createChartRequest(
    id: number,
    chartType: string,
    startDate: string,
    endDate: string
  ): ChartRequest {
    return {
      id: id,
      chartType: chartType,
      startDate: startDate,
      endDate: endDate,
    };
  }
}
