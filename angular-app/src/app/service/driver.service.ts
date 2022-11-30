import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Driver } from '../model/user/driver';
import { DriverActivityStatusRequest } from '../model/user/user-profile-update';
import { BehaviorSubject, Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root',
})
export class DriverService {

  public currentDriver$: BehaviorSubject<Driver>;

  constructor(
    private http: HttpClient, 
    private configService: ConfigService,
    private toast: ToastrService
    ) {
      this.currentDriver$ = new BehaviorSubject<Driver>(null);
    }

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

  showActivityStatusResetNotification(notification: DriverActivityStatusRequest): void {
    this.toast.info("Your activity status is changed to not active.", 'Working overrtime!');
    let driver: Driver = this.currentDriver$.value;
    driver.active = notification.active;
    this.currentDriver$.next(driver);
  }

  setGlobalDriver(driver: Driver): void {
    this.currentDriver$.next(driver);
  }

  getGlobalDriver(): BehaviorSubject<Driver> {
    return this.currentDriver$;
  }

  resetGlobalDriver(): void {
    this.currentDriver$.next(null);
  }

}
