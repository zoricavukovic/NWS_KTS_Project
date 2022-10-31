import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationResponse } from '../model/registration-reponse';
import { RegistrationRequest } from '../model/registration-request';
import { ConfigService } from './config.service';
import { map } from 'rxjs/operators';
import { VehicleTypeInfo } from '../model/vehicle-type-info-response';
import { DriverRegistrationRequest } from '../model/driver-registration-request';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) { }

  registerRegularUser(registrationRequest: RegistrationRequest){
    console.log(registrationRequest);

    return this.http.post<RegistrationResponse>(this.configService.registration_url, registrationRequest)
    .pipe(
      map((response) => { 
          console.log(response);
        }
      )
    );
  }

  registerDriver(driverRequest: DriverRegistrationRequest){
    console.log(driverRequest);

    return this.http.post<any>(this.configService.register_driver, driverRequest)
    .pipe(
      map((response) => { 
          console.log(response);
        }
      )
    );
  }

  getVehicleTypeInfos() {

    return this.http.get<[VehicleTypeInfo]>(this.configService.vehicle_type_infos);
  }

}
