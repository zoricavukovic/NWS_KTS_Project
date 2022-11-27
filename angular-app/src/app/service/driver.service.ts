import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Driver } from '../model/user/driver';
import { DriverActivityStatusRequest } from '../model/user/user-profile-update';
import { Observable } from 'rxjs';
@Injectable({
  providedIn: 'root',
})
export class DriverService {
  constructor(private http: HttpClient, private configService: ConfigService) {}

  createDriverUpdateActivityRequest(id: number, active: boolean): DriverActivityStatusRequest {

    return {id: id, active: active};
  }

  getAllDrivers() {
    return this.http.get<Driver[]>(this.configService.all_drivers_url, {
      headers: this.configService.getHeader(),
    });
  }

  getDriver(driverId: number) {
    return this.http.get<Driver>(this.configService.driver_info_url(driverId), {
      headers: this.configService.getHeader(),
    });
  }

  updateActivityStatus(data: DriverActivityStatusRequest): Observable<Driver> {
    return this.http.put<Driver>(this.configService.driver_update_activity, data, {
      headers: this.configService.getHeader(),
    });
  }

}
