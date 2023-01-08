import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { Driver } from '../model/user/driver';
import { DriverActivityStatusRequest } from '../model/user/user-profile-update';
import { BehaviorSubject, Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { GenericService } from './generic.service';

@Injectable({
  providedIn: 'root',
})
export class DriverService extends GenericService<Driver> {
  public currentDriver$: BehaviorSubject<Driver>;

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private toast: ToastrService
  ) {
    super(http, `${configService.api_url}/drivers`);
    this.currentDriver$ = new BehaviorSubject<Driver>(null);
  }

  createDriverUpdateActivityRequest(
    id: number,
    active: boolean
  ): DriverActivityStatusRequest {
    return { id: id, active: active };
  }

  updateActivityStatus(data: DriverActivityStatusRequest): Observable<Driver> {
    return this.http.put<Driver>(
      this.configService.driver_update_activity,
      data
    );
  }

  showActivityStatusResetNotification(
    notification: DriverActivityStatusRequest
  ): void {
    this.toast.info(
      'Your activity status is changed to not active.',
      'Working overrtime!'
    );
    const driver: Driver = this.currentDriver$.value;
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
