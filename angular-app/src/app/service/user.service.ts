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
import { User } from '../model/response/user/user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) { }

  sendResetPasswordEmail(email: string): Observable<boolean> {
    return this.http.get<boolean>(
      this.configService.send_reset_password_email(email)
    );
  }

  resetPassword(
    passwordUpdateRequest: PasswordUpdateRequest
  ): Observable<User> {
    return this.http.put<User>(
      this.configService.reset_password,
      passwordUpdateRequest,
      {headers: this.configService.header}
    );
  }

  updateProfileData(data: UsersProfileUpdateRequest): Observable<User> {
    return this.http.put<User>(this.configService.users_url, data, {headers: this.configService.header});
  }

  updateProfilePicture(data: UserProfilePictureRequest): Observable<User> {
    return this.http.put<User>(
      this.configService.users_update_profile_pic,
      data,
      {headers: this.configService.header}
    );
  }

  updatePassword(data: UserPasswordUpdateRequest): Observable<User> {
    return this.http.put<User>(this.configService.users_update_password, data, {headers: this.configService.header});
  }

  registerRegularUser(
    registrationRequest: RegistrationRequest
  ): Observable<User> {
    return this.http.post<User>(
      this.configService.registration_url,
      registrationRequest, {headers: this.configService.header}
    );
  }

  registerDriver(driverRequest: DriverRegistrationRequest): Observable<User> {
    return this.http.post<User>(
      this.configService.register_driver,
      driverRequest,
      {headers: this.configService.header}
    );
  }

  addToFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest) {
    return this.http.post<any>(
      this.configService.add_favourite_route_url,
      favouriteRouteRequest,
      {headers: this.configService.header}
    );
  }

  removeFromFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest) {
    return this.http.post<any>(
      this.configService.remove_favourite_route_url,
      favouriteRouteRequest,
      {headers: this.configService.header}
    );
  }

  isFavouriteRouteForUser(routeId: number, userId: number) {
    return this.http.get(
      this.configService.is_favourite_route(routeId, userId), {headers: this.configService.header}
    );
  }

  getAllRegularUsers() {
    return this.http.get<User[]>(this.configService.all_users_url, {headers: this.configService.header});
  }
}
