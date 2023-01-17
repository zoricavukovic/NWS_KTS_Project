import { HttpClient } from '@angular/common/http';
import {Injectable} from '@angular/core';
import { Observable } from 'rxjs';
import {GenericService} from "../generic-service/generic.service";
import {User} from "../../models/user/user";
import {PasswordUpdateRequest, UserProfilePictureRequest} from "../../models/user/user-profile-update";
import {ConfigService} from "../config-service/config.service";
import {UserDetails} from "../../models/user/user-details";
import {BlockNotification} from "../../models/notification/block-notification";
import {RegularUser} from "../../models/user/regular-user";
import {RegistrationResponse} from "../../models/user/registration-response";
import {Driver} from "../../models/user/driver";
import {FavouriteRouteRequest} from "../../models/route/favourite-route-request";
import {Role} from "../../models/user/role";

@Injectable({
  providedIn: 'root',
})
export class UserService extends GenericService<User> {

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    super(http, configService.USERS_URL);
  }

  sendResetPasswordEmail(email: string): Observable<boolean> {
    return this.http.get<boolean>(
      this.configService.sendResetPassword(email)
    );
  }

  resetPassword(
    passwordUpdateRequest: PasswordUpdateRequest
  ): Observable<User> {
    return this.http.put<User>(
      this.configService.RESET_PASSWORD_URL,
      passwordUpdateRequest
    );
  }

  updateProfileData(data: UserDetails): Observable<User> {

    return this.http.put<User>(this.configService.USERS_URL, data);
  }

  blockUser(data: BlockNotification): Observable<boolean> {

    return this.http.put<boolean>(this.configService.BLOCK_USER_URL, data);
  }

  unblockUser(id: number, isDriver: boolean): Observable<boolean> {
    return this.http.put<boolean>(
      isDriver
        ? this.configService.unblockDriverByIdUrl(id)
        : this.configService.unblockUserByIdUrl(id),
      null
    );
  }

  getBlockedData(id: number, isDriver: boolean): Observable<boolean> {
    return this.http.get<boolean>(
      isDriver
        ? this.configService.blockedDriverByIdUrl(id)
        : this.configService.blockedUserByIdUrl(id)
    );
  }

  updateProfilePicture(data: UserProfilePictureRequest): Observable<User> {

    return this.http.put<User>(this.configService.UPDATE_PROFILE_PICTURE_URL, data);
  }

  updatePassword(data: PasswordUpdateRequest): Observable<User> {

    return this.http.put<User>(this.configService.UPDATE_PASSWORD_URL, data);
  }

  registerRegularUser(
    registrationRequest: RegularUser
  ): Observable<RegistrationResponse> {
    return this.http.post<RegistrationResponse>(
      this.configService.CREATE_REGULAR_USER_URL,
      registrationRequest
    );
  }

  registerDriver(driverRequest: Driver): Observable<User> {

    return this.http.post<User>(this.configService.CREATE_DRIVER_URL, driverRequest);
  }

  addToFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest) {

    return this.http.post<boolean>(this.configService.SET_FAVOURITE_ROUTE_URL, favouriteRouteRequest);
  }

  updateFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest) {
    return this.http.post<boolean>(
      this.configService.SET_FAVOURITE_ROUTE_URL,
      favouriteRouteRequest
    );
  }

  isFavouriteRouteForUser(routeId: number, userId: number) {
    return this.http.get(
      this.configService.isFavouriteRouteUrl(routeId, userId)
    );
  }

  getUserByEmail(email: string): Observable<User> {

    return this.http.get<User>(this.configService.userByEmailUrl(email));
  }

  getAllVerified(): Observable<User[]> {

    return this.http.get<User[]>(this.configService.ALL_VERIFIED_URL);
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
