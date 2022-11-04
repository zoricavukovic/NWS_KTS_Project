import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationResponse } from '../model/registration-reponse';
import { RegistrationRequest } from '../model/registration-request';
import { ConfigService } from './config.service';
import { map } from 'rxjs/operators';
import { DriverRegistrationRequest } from '../model/driver-registration-request';
import { PasswordUpdateRequest } from '../model/password-update-request';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) { }

  sendResetPasswordEmail(email: string): void {
    
    this.http.get(this.configService.send_reset_password_email(email))
        .subscribe(
            data => console.log('success', data),
            error => console.log('oops', error)
        );
  }

  resetPassword(passwordUpdateRequest: PasswordUpdateRequest) {
    this.http.put(this.configService.reset_password, passwordUpdateRequest)
        .subscribe(
            data => console.log('success', data),
            error => console.log('oops', error)
        );
  }

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

}
