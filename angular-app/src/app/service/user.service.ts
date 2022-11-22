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

@Injectable({
  providedIn: 'root',
})
export class UserService {
  constructor(private http: HttpClient, private configService: ConfigService) {}

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
      { headers: this.configService.getHeader() }
    );
  }

  updateProfileData(data: UserDetails): Observable<User> {
    return this.http.put<User>(this.configService.users_url, data, {
      headers: this.configService.getHeader(),
    });
  }

  updateProfilePicture(data: UserProfilePictureRequest): Observable<User> {
    return this.http.put<User>(
      this.configService.users_update_profile_pic,
      data,
      { headers: this.configService.getHeader() }
    );
  }

  updatePassword(data: PasswordUpdateRequest): Observable<User> {
    return this.http.put<User>(this.configService.users_update_password, data, {
      headers: this.configService.getHeader(),
    });
  }

  registerRegularUser(registrationRequest: RegularUser): Observable<User> {
    return this.http.post<User>(
      this.configService.registration_url,
      registrationRequest,
      { headers: this.configService.getHeader() }
    );
  }

  registerDriver(driverRequest: Driver): Observable<User> {
    return this.http.post<User>(
      this.configService.register_driver,
      driverRequest,
      { headers: this.configService.getHeader() }
    );
  }

  addToFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest) {
    return this.http.post<any>(
      this.configService.add_favourite_route_url,
      favouriteRouteRequest,
      { headers: this.configService.getHeader() }
    );
  }

  removeFromFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest) {
    return this.http.post<any>(
      this.configService.remove_favourite_route_url,
      favouriteRouteRequest,
      { headers: this.configService.getHeader() }
    );
  }

  isFavouriteRouteForUser(routeId: number, userId: number) {
    return this.http.get(
      this.configService.is_favourite_route(routeId, userId),
      { headers: this.configService.getHeader() }
    );
  }

  getAllRegularUsers() {
    return this.http.get<User[]>(this.configService.all_users_url, {
      headers: this.configService.getHeader(),
    });
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
