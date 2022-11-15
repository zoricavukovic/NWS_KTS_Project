import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  header = new HttpHeaders().set("Authorization", localStorage.getItem('token'));

  public role_driver = "ROLE_DRIVER";
  public role_regular_user = "ROLE_REGULAR_USER";

  private _api_url = environment.apiUrl;
  private _login_user = this._api_url + "/auth/login";
  private _logout_user = this._api_url + "/auth/logout";
  private _login_with_gmail_user = this._api_url + "/auth/login/google";
  private _login_with_facebook_user = this._api_url + "/auth/login/facebook";
  private _register_user = this._api_url + "/regular-users/register";
  private _register_driver = this._api_url + "/drivers/register"
  private _verify_url = this._api_url + "/verify";
  private _send_verify_code_again = this._api_url + "/verify/send-code-again";
  private _vehicle_type_infos = this._api_url + "/vehicle-type-infos";
  private _drivings_url = this._api_url + "/drivings/";
  private _drivings_details_url = this._api_url + "/drivings/details/";
  private _driver_info_url = this._api_url + "/drivers/";
  private _vehicle_rate_url = this._api_url + "/vehicles/rating/";
  private _rate_vehicle_driver_url = this._api_url + "/reviews";
  private _have_driving_rate_url = this._api_url + "/reviews/haveDrivingRate/";
  private _reviewed_drivings_url = this._api_url + "/reviews/reviewedDrivings/";
  private _base64_show_photo_prefix = 'data:image/png;base64,';
  private _users_url = this._api_url + "/users";
  private _all_messages = this._api_url + "/messages"
  private _all_chat_rooms = this._api_url + "/chat-rooms"

  private _routes_url = this._api_url + "/routes";

  private _add_favourite_route_url = this._api_url + "/regular-users/favourite";
  private _remove_favourite_route_url = this._api_url + "/regular-users/removeFavourite";
  private _is_favourite_route_url = this._api_url + "/regular-users/favourite-route/";
  private _all_drivers_url = this._api_url + "/drivers";
  private _all_users_url = this._api_url + "/regular-users";
  private _get_favourite_routes = this._api_url +"/regular-users/favourite-routes/"

  private _send_reset_password_email = this._api_url + "/users/send-rest-password-link";
  private _reset_password = this._api_url + "/users/reset-password";

  get login_url(): string {
    return this._login_user;
  }

  get login_with_gmail_url(): string {
    return this._login_with_gmail_user;
  }

  get login_with_facebook_url(): string {
    return this._login_with_facebook_user;
  }

  get registration_url(): string{
    return this._register_user;
  }

  get verify_url(): string {
    return this._verify_url;
  }

  get send_verify_code_url(): string {
    return this._send_verify_code_again;
  }

  get vehicle_type_infos(): string {
    return this._vehicle_type_infos;
  }

  get register_driver() : string {
    return this._register_driver;
  }

  drivings_url(id: number, pageNumber:number, pageSize:number, parameter: string, sortOrder: string): string {
    return this._drivings_url + id+"/" +pageNumber+"/"+pageSize + "/" + parameter + "/" + sortOrder;
  }

  driving_details_url(id: number): string{
    return this._drivings_details_url + id;
  }

  get reset_password() : string {
    return this._reset_password;
  }

  driver_info_url(email: string): string{
    return this._driver_info_url + email;
  }

  get vehicle_rating_url(): string{
    return this._vehicle_rate_url;
  }

  get rate_driver_vehicle_url(): string{
    return this._rate_vehicle_driver_url;
  }

  get have_driving_rate_url(): string{
    return this._have_driving_rate_url;
  }

  reviewed_drivings_url(id: number): string{
    return this._reviewed_drivings_url + id;
  };

  send_reset_password_email(email: string): string {
    return this._send_reset_password_email + "/" + email;
  }


  get base64_show_photo_prefix(): string {
    return this._base64_show_photo_prefix;
  }

  get users_url() : string {
    return this._users_url;
  }

  get users_update_profile_pic(): string {
    return this._users_url + "/profile-picture";
  }

  get users_update_password(): string {
    return this._users_url + "/password";
  }

  get all_messages(): string {
    return this._all_messages;
  }

  get logout_url(): string {
    return this._logout_user;
  }

  get chat_rooms_url(): string {
    return this._all_chat_rooms;
  }

  get all_chat_rooms_url(): string {
    return this._all_chat_rooms + '/all/';
  }

  get resolve_chat_room_url(): string {
    return this._all_chat_rooms + '/resolve/';
  }

  get option_routes(): string {
    return this._routes_url + "/possible";
  }

  get add_favourite_route_url(): string{
    return this._add_favourite_route_url;
  }

  get remove_favourite_route_url(): string{
    return this._remove_favourite_route_url;
  }

  is_favourite_route(id:number, email: string){
    return this._is_favourite_route_url + id + "/" + email;
  }

  get all_drivers_url(): string{
    return this._all_drivers_url;
  }

  get all_users_url(): string{
    return this._all_users_url;
  }

  get_favourite_routes(email: string): string{
    return this._get_favourite_routes + email;
  }

}
