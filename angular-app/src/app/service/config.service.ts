import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ConfigService {

  API_URL = environment.apiUrl;

  ///////////////////ADMIN///////////////////
  ADMINS_URL = `${this.API_URL}/admins`;
  
  adminByIdUrl(adminId: number): string {
    return `${this.ADMINS_URL}/${adminId}`;
  }

  ///////////////////AUTH///////////////////
  AUTH_URL = `${this.API_URL}/auth`;
  LOGIN_URL = `${this.AUTH_URL}/login`;
  GOOGLE_LOGIN_URL = `${this.LOGIN_URL}/google`;
  FACEBOOK_LOGIN_URL = `${this.LOGIN_URL}/facebook`;
  LOGOUT_URL = `${this.AUTH_URL}/logout`;

  ///////////////////DRIVER///////////////////

  DRIVERS_URL = `${this.API_URL}/drivers`;
  ACTIVITY_STATUS_DRIVERS_URL = `${this.DRIVERS_URL}/activity`;

  driverByIdUrl(driverId: number): string{
    return `${this.DRIVERS_URL}/${driverId}`;
  }

  blockedDriverByIdUrl(driverId: number): string{
    return `${this.DRIVERS_URL}/blocked-data/${driverId}`;
  }

  driverRatingByIdUrl(driverId: number): string{
    return `${this.DRIVERS_URL}/rating/${driverId}`;
  }

  unblockDriverByIdUrl(driverId: number): string{
    return `${this.DRIVERS_URL}//unblock/${driverId}`;
  }


  ///////////////////REGULAR USER///////////////////
  REGULAR_USERS_URL = `${this.API_URL}/regular-users`;
  SET_FAVOURITE_ROUTE_URL = `${this.REGULAR_USERS_URL}/favourite`

  blockedUserByIdUrl(userId: number): string{
    return `${this.REGULAR_USERS_URL}/blocked-data/${userId}`;
  }

  unblockUserByIdUrl(userId: number): string{
    return `${this.REGULAR_USERS_URL}//unblock/${userId}`;
  }

  isFavouriteRouteUrl(routeId: number, userId: number): string{
    return `${this.REGULAR_USERS_URL}/favourite-route/${routeId}/${userId}`;
  }

  allFavouriteRoutesUrl(userId: number): string{
    return `${this.REGULAR_USERS_URL}/favourite-routes/${userId}`
  }

  ///////////////////USER///////////////////

  USERS_URL = `${this.API_URL}/users`;
  UPDATE_PROFILE_PICTURE_URL = `${this.USERS_URL}/profile-picture`;
  UPDATE_PASSWORD_URL = `${this.USERS_URL}/password`;
  RESET_PASSWORD_URL = `${this.USERS_URL}/reset-password`;
  ACTIVATE_ACCOUNT_URL = `${this.USERS_URL}/activate-account`;
  BLOCK_USER_URL = `${this.USERS_URL}/block`;
  CREATE_DRIVER_URL = `${this.USERS_URL}/create/driver`;
  CREATE_REGULAR_USER_URL = `${this.USERS_URL}/create/regular-user`;
  
  userByIdUrl(id: number){
    return `${this.USERS_URL}/${id}`;
  }

  userByEmailUrl(email: string): string{
    return `${this.USERS_URL}/byEmail/{email}`;
  }


  ///////////////////DRIVING///////////////////

  DRIVINGS_URL = `${this.API_URL}/drivings`;

  drivingsSortPaginationUrl(userId: number, pageNumber: number, pageSize: number, parameter: string, sortOrder: string): string{
    return `${this.DRIVINGS_URL}/${userId}/${pageNumber}/${pageSize}/${parameter}/${sortOrder}`;
  }

  nowAndFutureDrivingsUrl(userId: number): string{
    return `${this.DRIVINGS_URL}/now-and-future/{id}`;
  }

  drivingByIdUrl(id: number): string{
    return `${this.DRIVINGS_URL}/${id}`;
  }

  finishDrivingUrl(id: number): string{
    return `${this.DRIVINGS_URL}/finish-driving/${id}`;
  }  

  startDrivingUrl(id: number): string{
    return `${this.DRIVINGS_URL}/start/${id}`;
  }  

  rejectDrivingUrl(id: number): string{
    return `${this.DRIVINGS_URL}/reject/${id}`;
  } 
  
  hasUserActiveDriving(userId: string): string{
    return `${this.DRIVINGS_URL}/has-active/user/${userId}`
  }

  ///////////////////DRIVING-NOTIFICATION///////////////////

  DRIVING_NOTIFICATIONS_URL = `${this.API_URL}/driving-notifications`;

  acceptDrivingUrl(drivingNotificationId: number, accepted: boolean, userEmail: string): string{
    return `${this.DRIVING_NOTIFICATIONS_URL}/update-status/${drivingNotificationId}/${accepted}/${userEmail}`;
  }

  ///////////////////LOCATION///////////////////

  LOCATIONS_URL = `${this.API_URL}/locations`;

  ///////////////////REPORT///////////////////

  REPORTS_URL = `${this.API_URL}/reports`;

  reportsForUserUrl(userId: number): string{
    return `${this.REPORTS_URL}/all-for-user/${userId}`;
  }

  ///////////////////REVIEW///////////////////

  REVIEW_URL = `${this.API_URL}/reviews`;
  
  reviewsForDriverUrl(driverId: number): string{
    return `${this.REVIEW_URL}/all-for-driver/${driverId}`;
  }

  deleteReviewUrl(id: number): string{
    return `${this.REVIEW_URL}/${id}`;
  }

  reviewedDrivingsForUser(userId: number): string{
    return `${this.REVIEW_URL}/reviewedDrivings/${userId}`;
  }


  ///////////////////ROUTE///////////////////

  ROUTES_URL = `${this.API_URL}/routes`;
  POSSIBLE_ROUTES_URL = `${this.ROUTES_URL}/possible`;

  routePathUrl(id: number): string{
    return `${this.ROUTES_URL}/path/${id}`;
  }


  ///////////////////VEHICLE///////////////////

  VEHICLES_URL = `${this.API_URL}/vehicles`;
  ACTIVE_VEHICLES_URL = `${this.VEHICLES_URL}/active`;
  UPDATE_CURRENT_LOCATION_URL = `${this.VEHICLES_URL}/update-current-location`;

  deleteVehicleUrl(id: number): string{
    return `${this.VEHICLES_URL}/${id}`;
  }

  ratingForVehicleUrl(id: number): string{
    return `${this.VEHICLES_URL}/rating/${id}`;
  }

  vehicleByVehicleTypeUrl(vehicleType: string): string{
    return `${this.VEHICLES_URL}/${vehicleType}`;
  }

  ///////////////////VEHICLE TYPE INFOS///////////////////
  VEHICLE_TYPE_INFOS_URL = `${this.API_URL}/vehicle-type-infos`;

  priceForRouteAndVehicleUrl(vehicleType: string, kilometers: string): string{
    return `${this.VEHICLE_TYPE_INFOS_URL}/price/${vehicleType}/${kilometers}`
  }

  ///////////////////VERIFY///////////////////

  VERIFY_URL = `${this.API_URL}/verify`;
  SEND_CODE_AGAIN_URL = `${this.VERIFY_URL}/send-code-again`;

  
  ///////////////////CHAT ROOM///////////////////

  CHAT_ROOMS_URL = `${this.API_URL}/chat-rooms`;
  RESOLVE_CHAT_URL = `${this.CHAT_ROOMS_URL}/resolve`;
  SET_MESSAGE_SEEN_URL = `${this.CHAT_ROOMS_URL}/seen-messages`;

  activeChatRoomsForUserUrl(userEmail: string): string{
    return `${this.CHAT_ROOMS_URL}/${userEmail}`;
  }

  allChatRoomsForUserUrl(userEmail: string): string{
    return `${this.CHAT_ROOMS_URL}/all/${userEmail}`;
  }

  ///////////////////PAYING INFO///////////////////

  PAYING_INFOS_URL = `${this.API_URL}/paying-infos`;

  payingInfoForUser(userId: number){
    return `${this.PAYING_INFOS_URL}/${userId};`
  }

  ///////////////////PAYPAL///////////////////

  PAYPAL_URL = `${this.API_URL}/paypal`;
  CREATE_PAYMENT_URL = `${this.PAYPAL_URL}/create-payment`;
  COMPLETE_PAYMENT_URL = `${this.PAYPAL_URL}/complete-payment`;

  ///////////////////TOKEN BANK///////////////////

  TOKEN_BANKS_URL = `${this.API_URL}/token-banks`;
  SPENDING_TOKENS_URL = `${this.TOKEN_BANKS_URL}/in-app-spending`;

  tokenBankForUser(userId: number): string{
    return `${this.TOKEN_BANKS_URL}/${userId}`;
  }


  // public role_driver = 'ROLE_DRIVER';
  // public role_regular_user = 'ROLE_REGULAR_USER';


  // private _drivings_url = `${this.api_url}/drivings`;
  // private _drivers_url = `${this.api_url}/drivers`;
  // private _drivings_finish_driving_url = `${this._drivings_url}/finish-driving`;
  // private _reject_driving_url = `${this._drivings_url}/reject`;
  // private _start_driving_url = `${this._drivings_url}/start`;

  // private _logout_user = this.api_url + '/auth/logout';
  // private _register_user = this.api_url + '/users/create/regular-user';
  // private _register_driver = this.api_url + '/users/create/driver';
  // private _verify_url = this.api_url + '/users/activate-account';
  // private _block_user_url = this.api_url + '/users/block';
  // private _blocked_data_regular_url =
  //   this.api_url + '/regular-users/blocked-data/';
  // private _blocked_data_driver_url = this.api_url + '/drivers/blocked-data/';
  // private _unblock_regular_url = this.api_url + '/regular-users/unblock/';
  // private _unblock_driver_url = this.api_url + '/drivers/unblock/';
  // private _send_verify_code_again = this.api_url + '/verify/send-code-again';
  // private _vehicle_type_infos = this.api_url + '/vehicle-type-infos';
  // private _token_bank_url = this.api_url + '/token-banks';
  // private _in_app_spending_url = this._token_bank_url + '/in-app-spending';
  // private _paypal_url = this.api_url + '/paypal'
  // private _create_payment = this._paypal_url + '/create-payment';
  // private _complete_payment = this._paypal_url + '/complete-payment';
  // private _paying_info = this._paypal_url + '/paying-infos';

  // private _drivings_pagination_url = this.api_url + '/drivings';
  // private _drivings_details_url = this.api_url + '/drivings/details/';
  // private _drivings_count_url = this.api_url + '/drivings/number/';
  // private _driver_info_url = this.api_url + '/drivers/';
  // private _vehicle_rate_url = this.api_url + '/vehicles/rating/';
  // private _all_active_vehicles_url: string = this.api_url + '/vehicles/active';
  // private _rate_vehicle_driver_url = this.api_url + '/reviews';
  // private _reviews_per_driver_url = this.api_url + '/reviews/all-for-driver';
  // private _have_driving_rate_url = this.api_url + '/reviews/haveDrivingRate/';
  // private _reviewed_drivings_url = this.api_url + '/reviews/reviewedDrivings/';
  // private _base64_show_photo_prefix = 'data:image/png;base64,';
  // private _users_url = this.api_url + '/users';
  // private _all_messages = this.api_url + '/messages';
  // private _all_chat_rooms = this.api_url + '/chat-rooms';

  // private _routes_url = this.api_url + '/routes';

  // private _add_favourite_route_url = this.api_url + '/regular-users/favourite';
  // private _remove_favourite_route_url =
  //   this.api_url + '/regular-users/removeFavourite';
  // private _is_favourite_route_url =
  //   this.api_url + '/regular-users/favourite-route/';
  // private _all_drivers_url = this.api_url + '/drivers';
  // private _all_users_url = this.api_url + '/regular-users';
  // private _get_favourite_routes =
  //   this.api_url + '/regular-users/favourite-routes/';

  // private _reports_for_user_url = this.api_url + '/reports/all-for-user/';

  // private _send_reset_password_email =
  //   this.api_url + '/users/send-rest-password-link';
  // private _reset_password = this.api_url + '/users/reset-password';

  // private _price_for_driving_url = this.api_url + '/vehicle-type-infos/price';

  // private _driving_notifications_url = this.api_url + '/driving-notifications';

  // private _accept_driving_url = this.api_url + '/driving-notifications/accept/';

  // private _vehicle_url = this.api_url + '/vehicles/';

  // get_vehicle_by_vehicle_type(vehicleType: string): string {
  //   return this._vehicle_url + vehicleType;
  // }

  // get login_url(): string {
  //   return this._login_user;
  // }

  // get login_with_gmail_url(): string {
  //   return this._login_with_gmail_user;
  // }

  // get login_with_facebook_url(): string {
  //   return this._login_with_facebook_user;
  // }

  // get registration_url(): string {
  //   return this._register_user;
  // }

  // get verify_url(): string {
  //   return this._verify_url;
  // }

  // get send_verify_code_url(): string {
  //   return this._send_verify_code_again;
  // }

  // get vehicle_type_infos(): string {
  //   return this._vehicle_type_infos;
  // }

  // get register_driver(): string {
  //   return this._register_driver;
  // }

  // drivings_url_with_pagination_and_sort(
  //   id: number,
  //   pageNumber: number,
  //   pageSize: number,
  //   parameter: string,
  //   sortOrder: string
  // ): string {
  //   return (
  //     this._drivings_pagination_url +
  //     '/' +
  //     id +
  //     '/' +
  //     pageNumber +
  //     '/' +
  //     pageSize +
  //     '/' +
  //     parameter +
  //     '/' +
  //     sortOrder
  //   );
  // }

  // now_future_drivings_url(id: number): string {
  //   return this._drivings_pagination_url + '/now-and-future/' + id;
  // }

  // check_user_has_active_driving_url(id: number): string {
  //   return `${this._drivings_pagination_url}/has-active/user/${id}`;
  // }

  // driving_details_url(id: number): string {
  //   return this._drivings_details_url + id;
  // }

  // get reset_password(): string {
  //   return this._reset_password;
  // }

  // driver_info_url(id: number): string {
  //   return this._driver_info_url + id;
  // }

  // get vehicle_rating_url(): string {
  //   return this._vehicle_rate_url;
  // }

  // get all_active_vehicles_url(): string {
  //   return this._all_active_vehicles_url;
  // }

  // get rate_driver_vehicle_url(): string {
  //   return this._rate_vehicle_driver_url;
  // }

  // get have_driving_rate_url(): string {
  //   return this._have_driving_rate_url;
  // }

  // reviewed_drivings_url(id: number): string {
  //   return this._reviewed_drivings_url + id;
  // }

  // send_reset_password_email(email: string): string {
  //   return this._send_reset_password_email + '/' + email;
  // }

  // get base64_show_photo_prefix(): string {
  //   return this._base64_show_photo_prefix;
  // }

  // get users_url(): string {
  //   return this._users_url;
  // }

  // get users_update_profile_pic(): string {
  //   return this._users_url + '/profile-picture';
  // }

  // get users_update_password(): string {
  //   return this._users_url + '/password';
  // }

  // get driver_update_activity(): string {
  //   return `${this._drivers_url}/activity`;
  // }

  // get all_messages(): string {
  //   return this._all_messages;
  // }

  // get logout_url(): string {
  //   return this._logout_user;
  // }

  // get chat_rooms_url(): string {
  //   return this._all_chat_rooms;
  // }

  // get all_chat_rooms_url(): string {
  //   return this._all_chat_rooms + '/all/';
  // }

  // get resolve_chat_room_url(): string {
  //   return this._all_chat_rooms + '/resolve';
  // }

  // get set_messages_as_seen(): string {
  //   return this._all_chat_rooms + '/seen-messages';
  // }

  // get option_routes(): string {
  //   return this._routes_url + '/possible';
  // }

  // routePathUrl(routeId: number): string {
  //   return this._routes_url + '/path/' + routeId;
  // }

  // get add_favourite_route_url(): string {
  //   return this._add_favourite_route_url;
  // }

  // get remove_favourite_route_url(): string {
  //   return this._remove_favourite_route_url;
  // }

  // is_favourite_route(route_id: number, user_id: number) {
  //   return this._is_favourite_route_url + route_id + '/' + user_id;
  // }

  // get all_drivers_url(): string {
  //   return this._all_drivers_url;
  // }

  // get all_users_url(): string {
  //   return this._all_users_url;
  // }

  // user_by_id_url(id: string): string {
  //   return `${this._users_url}/${id}`;
  // }

  // token_bank_by_user_id_url(id: string): string {
  //   return `${this._token_bank_url}/${id}`;
  // }

  // get_user_by_email(email: string): string {
  //   return `${this._users_url}/byEmail/${email}`;
  // }

  // get_count_drivings(id: number): string {
  //   return this._drivings_count_url + id;
  // }

  // get_favourite_routes(userId: number): string {
  //   return this._get_favourite_routes + userId;
  // }

  // finish_driving_url(drivingId: number): string {
  //   return `${this._drivings_finish_driving_url}/${drivingId}`;
  // }

  // reject_driving_url(drivingId: number): string {
  //   return `${this._reject_driving_url}/${drivingId}`;
  // }

  // start_driving_url(drivingId: number) {
  //   return `${this._start_driving_url}/${drivingId}`;
  // }

  // get_price_for_driving(type: string, kilometers: number) {
  //   return this._price_for_driving_url + '/' + type + '/' + kilometers;
  // }

  // get_reviews_for_driver(driverId: number): string {
  //   return `${this._reviews_per_driver_url}/${driverId}`;
  // }

  // get_reports_for_user(id: number): string {
  //   return `${this._reports_for_user_url}${id}`;
  // }

  // get_blocked_data_regular_url(id: number): string {
  //   return `${this._blocked_data_regular_url}${id}`;
  // }

  // get_blocked_data_driver_url(id: number): string {
  //   return `${this._blocked_data_driver_url}${id}`;
  // }

  // get_unblock_regular_url(id: number): string {
  //   return `${this._unblock_regular_url}${id}`;
  // }

  // get_unblock_driver_url(id: number): string {
  //   return `${this._unblock_driver_url}${id}`;
  // }

  // get driving_notifications_url(): string {
  //   return this._driving_notifications_url;
  // }

  // get_accept_driving_url(id: number): string {
  //   return this._accept_driving_url + id;
  // }

  // get block_user_url(): string {
  //   return this._block_user_url;
  // }

  // get create_payment_url(): string {
  //   return this._create_payment;
  // }

  // get complete_payment_url(): string {
  //   return this._complete_payment;
  // }

  // get in_app_spending(): string {
  //   return this._in_app_spending_url;
  // }
}
