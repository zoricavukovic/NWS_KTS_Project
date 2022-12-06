import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Observable } from 'rxjs';
import { Driving } from '../model/driving/driving';
import { GenericService } from './generic.service';
import { HeadersService } from './headers.service';

@Injectable({
  providedIn: 'root',
})
export class DrivingService extends GenericService<Driving> {
  constructor(
    private http: HttpClient,
    private headersService: HeadersService,
    private configService: ConfigService
  ) {
    super(http, `${configService.api_url}/drivings`, headersService);
  }

  getDrivingsForUser(
    id: number,
    pageNumber: number,
    pageSize: number,
    selectedSortBy: string,
    selectedSortOrder: string
  ) {
    console.log(this.configService.getHeader());
    return this.http.get(
      this.configService.drivings_url_with_pagination_and_sort(
        id,
        pageNumber,
        pageSize,
        selectedSortBy,
        selectedSortOrder
      ),
      { headers: this.configService.getHeader() }
    );
  }

  getDrivingsForDriver(driverId: number): Observable<Driving[]> {
    return this.http.get<Driving[]>(
      this.configService.now_future_drivings_url(driverId),
      { headers: this.configService.getHeader() }
    );
  }

  getCountDrivings(id: number) {
    return this.http.get<number>(this.configService.get_count_drivings(id), {
      headers: this.configService.getHeader(),
    });
  }

  rejectDriving(drivingId: number, reason: string): Observable<Driving> {
    return this.http.put<Driving>(
      this.configService.reject_driving_url(drivingId),
      reason,
      {
        headers: this.configService.getHeader(),
      }
    );
  }
}
