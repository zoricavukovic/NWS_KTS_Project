import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegularUser } from '../model/user/regular-user';
import { ConfigService } from './config.service';
import { Driver } from '../model/user/driver';
import { UserProfilePictureRequest } from '../model/user/user-profile-update';
import { UserDetails } from '../model/user/user-details';
import { PasswordUpdateRequest } from 'src/app/model/user/user-profile-update';
import { FavouriteRouteRequest } from '../model/route/favourite-route-request';
import { User } from '../model/user/user';
import { Observable } from 'rxjs';
import { Role } from '../model/user/role';
import { RegistrationResponse } from '../model/user/registration-response';
import { BlockNotification } from '../model/notification/block-notification';
import { GenericService } from './generic.service';

@Injectable({
  providedIn: 'root',
})
export class UserService extends GenericService<User> {
  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    super(http, `${configService.api_url}/users`);
  }

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
      passwordUpdateRequest
    );
  }

  updateProfileData(data: UserDetails): Observable<User> {

    return this.http.put<User>(this.configService.users_url, data);
  }

  blockUser(data: BlockNotification): Observable<boolean> {

    return this.http.put<boolean>(this.configService.block_user_url, data);
  }

  unblockUser(id: number, isDriver: boolean): Observable<boolean> {
    return this.http.put<boolean>(
      isDriver
        ? this.configService.get_unblock_driver_url(id)
        : this.configService.get_unblock_regular_url(id),
      null
    );
  }

  getBlockedData(id: number, isDriver: boolean): Observable<boolean> {
    return this.http.get<boolean>(
      isDriver
        ? this.configService.get_blocked_data_driver_url(id)
        : this.configService.get_blocked_data_regular_url(id)
    );
  }

  updateProfilePicture(data: UserProfilePictureRequest): Observable<User> {

    return this.http.put<User>(this.configService.users_update_profile_pic, data);
  }

  updatePassword(data: PasswordUpdateRequest): Observable<User> {

    return this.http.put<User>(this.configService.users_update_password, data);
  }

  registerRegularUser(
    registrationRequest: RegularUser
  ): Observable<RegistrationResponse> {
    return this.http.post<RegistrationResponse>(
      this.configService.registration_url,
      registrationRequest
    );
  }

  registerDriver(driverRequest: Driver): Observable<User> {

    return this.http.post<User>(this.configService.register_driver, driverRequest);
  }

  addToFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest) {

    return this.http.post<boolean>(this.configService.add_favourite_route_url, favouriteRouteRequest);
  }

  updateFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest) {
    return this.http.post<boolean>(
      this.configService.add_favourite_route_url,
      favouriteRouteRequest
    );
  }

  isFavouriteRouteForUser(routeId: number, userId: number) {
    return this.http.get(
      this.configService.is_favourite_route(routeId, userId)
    );
  }

  getUserByEmail(email: string): Observable<User> {

    return this.http.get<User>(this.configService.get_user_by_email(email));
  }

  createUserDetails(
    email: string,
    name: string,
    surname: string,
    phoneNumber?: string,
    city?: string,
    role?: Role
  ): UserDetails {
    return {
      email: email,
      name: name,
      surname: surname,
      phoneNumber: phoneNumber,
      city: city,
      role: role,
    };
  }

  createFavouriteRequest(
    userId: number,
    routeId: number
  ): FavouriteRouteRequest {
    return {
      userId: userId,
      routeId: routeId,
    };
  }
}
