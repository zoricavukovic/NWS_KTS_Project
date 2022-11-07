import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationResponse } from '../model/registration-reponse';
import { RegistrationRequest } from '../model/registration-request';
import { ConfigService } from './config.service';
import { map } from 'rxjs/operators';
import { DriverRegistrationRequest } from '../model/driver-registration-request';
import { PasswordUpdateRequest } from '../model/password-update-request';
import { UserProfilePictureRequest } from '../model/user-profile-picture-request';
import { UsersProfileUpdateRequest } from '../model/users-profile-update-request';
import { UserPasswordUpdateRequest } from '../model/user-password-update-request';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private router: Router,
    private authService: AuthService
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

  updateProfileData(data: UsersProfileUpdateRequest) {
    this.http.put(this.configService.users_url, data)
        .subscribe(
            res => {
              //this.router.navigate(['/profile-page'], {state: {user: res}});
              this.router.navigate(['/profile-page']);
              const parsedUser = res as User;
              this.authService.setUserInLocalStorage(parsedUser);
            },
            error => console.log('oops', error)
        );
  }

  updateProfilePicture(data: UserProfilePictureRequest) {
    this.http.put(this.configService.users_update_profile_pic, data)
        .subscribe(
            data => console.log('success', data),
            error => console.log('oops', error)
        );
  }

  updatePassword(data: UserPasswordUpdateRequest) {
    this.http.put(this.configService.users_update_password, data)
        .subscribe(
            data => {
              this.authService.logOut()
              this.router.navigate(['/login']);
              console.log('success', data);},
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
