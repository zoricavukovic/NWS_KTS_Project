import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Driver } from '../model/response/user/driver';
@Injectable({
  providedIn: 'root',
})
export class DriverService {
  constructor(private http: HttpClient, private configService: ConfigService) {}

  getAllDrivers() {
    return this.http.get<Driver[]>(this.configService.all_drivers_url, {headers: this.configService.getHeader()});
  }

  getDriver(driverId: number) {
    return this.http.get<Driver>(this.configService.driver_info_url(driverId), {headers: this.configService.getHeader()});
  }
}
