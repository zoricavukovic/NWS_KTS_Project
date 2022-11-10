import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationRequest } from '../model/request/user/registration-request';
import { ConfigService } from './config.service';
import { DriverRegistrationRequest } from '../model/request/user/driver-registration-request';
import { PasswordUpdateRequest } from 'src/app/model/request/user/user-profile-update';
import { UserProfilePictureRequest } from '../model/request/user/user-profile-update';
import { UsersProfileUpdateRequest } from '../model/request/user/user-profile-update';
import { UserPasswordUpdateRequest } from '../model/request/user/user-profile-update';
import { FavouriteRouteRequest } from '../model/request/favourite-route-request';
import { Router } from '@angular/router';
import { AuthService } from './auth.service';
import { User } from '../model/response/user/user';
import { Observable } from 'rxjs';


@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private router: Router,
    private authService: AuthService,
  ) { }

  sendResetPasswordEmail(email: string): Observable<boolean> {
    
    return this.http.get<boolean>(this.configService.send_reset_password_email(email));
  }

  resetPassword(passwordUpdateRequest: PasswordUpdateRequest): Observable<User> {

    return this.http.put<User>(this.configService.reset_password, passwordUpdateRequest);
  }

  updateProfileData(data: UsersProfileUpdateRequest): Observable<User> {

    return this.http.put<User>(this.configService.users_url, data);
  }

  updateProfilePicture(data: UserProfilePictureRequest): Observable<User> {

    return this.http.put<User>(this.configService.users_update_profile_pic, data);
  }

  updatePassword(data: UserPasswordUpdateRequest): Observable<User> {

    return this.http.put<User>(this.configService.users_update_password, data)
  }

  registerRegularUser(registrationRequest: RegistrationRequest): Observable<User>{

    return this.http.post<User>(this.configService.registration_url, registrationRequest)
  }

  registerDriver(driverRequest: DriverRegistrationRequest): Observable<User> {

    return this.http.post<User>(this.configService.register_driver, driverRequest)
  }

  addToFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest){

    return this.http.post<any>(this.configService.add_favourite_route_url, favouriteRouteRequest)
  }

  removeFromFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest){

    return this.http.post<any>(this.configService.remove_favourite_route_url, favouriteRouteRequest)
  }

  isFavouriteRouteForUser(routeId:number, email: string){

    return this.http.get(this.configService.is_favourite_route(routeId,email));
  }

}
