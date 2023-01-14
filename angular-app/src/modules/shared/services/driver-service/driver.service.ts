import { Injectable } from '@angular/core';
import {HttpClient} from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import {ConfigService} from "../config-service/config.service";
import {GenericService} from "../generic-service/generic.service";
import {DriverActivityStatusRequest} from "../../models/user/user-profile-update";
import {Driver} from "../../models/user/driver";

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
    super(http, configService.DRIVERS_URL);
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
      this.configService.ACTIVITY_STATUS_DRIVERS_URL,
      data
    );
  }

  getDriverRate(id: number): Observable<number> {
    return this.http.get<number>(
      this.configService.driverRatingByIdUrl(id)
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
