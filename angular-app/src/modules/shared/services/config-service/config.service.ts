import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ConfigService {
  API_URL = environment.apiUrl;
  BASE64_PHOTO_PREFIX = 'data:image/png;base64,';
  ROLE_DRIVER = 'ROLE_DRIVER';
  ROLE_REGULAR_USER = 'ROLE_REGULAR_USER';

  TODAY = new Date();
  MONTH = this.TODAY.getMonth();
  YEAR = this.TODAY.getFullYear();

  SELECTED_SPENDING_REPORT = 'SPENDING';
  SELECTED_RIDES_REPORT = 'RIDES';
  SELECTED_DISTANCE_REPORT = 'DISTANCE';

  ///////////////////ADMIN///////////////////
  ADMINS_URL = `${this.API_URL}/admins`;

  adminByIdUrl(adminId: number): string {
    return `${this.ADMINS_URL}/${adminId}`;
  }

  ///////////////////AUTH///////////////////
  AUTH_URL = `${this.API_URL}/auth`;
  LOGIN_URL = `${this.AUTH_URL}/login`;

  getLoginUrl(): string {
    return this.LOGIN_URL;
  }

  getGoogleLoginUrl(): string {
    return this.GOOGLE_LOGIN_URL;
  }

  getFacebookLoginUrl(): string {
    return this.FACEBOOK_LOGIN_URL;
  }

  GOOGLE_LOGIN_URL = `${this.LOGIN_URL}/google`;
  FACEBOOK_LOGIN_URL = `${this.LOGIN_URL}/facebook`;
  LOGOUT_URL = `${this.AUTH_URL}/logout`;

  ///////////////////DRIVER///////////////////

  DRIVERS_URL = `${this.API_URL}/drivers`;
  ACTIVITY_STATUS_DRIVERS_URL = `${this.DRIVERS_URL}/activity`;

  driverByIdUrl(driverId: number): string {
    return `${this.DRIVERS_URL}/${driverId}`;
  }

  blockedDriverByIdUrl(driverId: number): string {
    return `${this.DRIVERS_URL}/blocked-data/${driverId}`;
  }

  driverRatingByIdUrl(driverId: number): string {
    return `${this.DRIVERS_URL}/rating/${driverId}`;
  }

  unblockDriverByIdUrl(driverId: number): string {
    return `${this.DRIVERS_URL}/unblock/${driverId}`;
  }

  ///////////////////REGULAR USER///////////////////
  REGULAR_USERS_URL = `${this.API_URL}/regular-users`;
  SET_FAVOURITE_ROUTE_URL = `${this.REGULAR_USERS_URL}/favourite`;

  blockedUserByIdUrl(userId: number): string {
    return `${this.REGULAR_USERS_URL}/blocked-data/${userId}`;
  }

  unblockUserByIdUrl(userId: number): string {
    return `${this.REGULAR_USERS_URL}/unblock/${userId}`;
  }

  isFavouriteRouteUrl(routeId: number, userId: number): string {
    return `${this.REGULAR_USERS_URL}/favourite-route/${routeId}/${userId}`;
  }

  allFavouriteRoutesUrl(userId: number): string {
    return `${this.REGULAR_USERS_URL}/favourite-routes/${userId}`;
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
  ALL_VERIFIED_URL = `${this.USERS_URL}/all-verified`;

  userByIdUrl(id: number) {
    return `${this.USERS_URL}/${id}`;
  }

  userByEmailUrl(email: string): string {
    return `${this.USERS_URL}/byEmail/${email}`;
  }

  sendResetPassword(email: string): string {
    return `${this.USERS_URL}/send-reset-password-link/${email}`;
  }

  getCreateRegularUserUrl(): string {
    return this.CREATE_REGULAR_USER_URL;
  }

  getCreateDriverUrl(): string {
    return this.CREATE_DRIVER_URL;
  }

  ///////////////////DRIVING///////////////////

  DRIVINGS_URL = `${this.API_URL}/drivings`;
  HAVE_PASSENGERS_ALREADY_RIDE_URL = `${this.DRIVINGS_URL}/busy-passengers`;

  drivingsSortPaginationUrl(
    userId: number,
    pageNumber: number,
    pageSize: number,
    parameter: string,
    sortOrder: string
  ): string {
    return `${this.DRIVINGS_URL}/${userId}/${pageNumber}/${pageSize}/${parameter}/${sortOrder}`;
  }

  drivingByFavouriteRoute(routeId: number) {
    return `${this.DRIVINGS_URL}/driving-by-favourite-route/${routeId}`;
  }

  nowAndFutureDrivingsUrl(userId: number): string {
    return `${this.DRIVINGS_URL}/now-and-future/${userId}`;
  }

  drivingByIdUrl(id: number): string {
    return `${this.DRIVINGS_URL}/${id}`;
  }

  finishDrivingUrl(id: number): string {
    return `${this.DRIVINGS_URL}/finish-driving/${id}`;
  }

  startDrivingUrl(id: number): string {
    return `${this.DRIVINGS_URL}/start/${id}`;
  }

  rejectDrivingUrl(id: number): string {
    return `${this.DRIVINGS_URL}/reject/${id}`;
  }

  hasUserActiveDriving(userId: number): string {
    return `${this.DRIVINGS_URL}/has-active/user/${userId}`;
  }

  vehicleCurrentLocation(drivingId: number): string {
    return `${this.DRIVINGS_URL}/vehicle-current-location/${drivingId}`;
  }

  getChartData(): string {
    return `${this.DRIVINGS_URL}/chart-data`;
  }

  getAdminChartData(): string {
    return `${this.DRIVINGS_URL}/admin-chart-data`;
  }

  getTimeForDrivingUrl(drivingId: number): string {
    return `${this.DRIVINGS_URL}/time-for-driving/${drivingId}`;
  }

  ///////////////////DRIVING-NOTIFICATION///////////////////

  DRIVING_NOTIFICATIONS_URL = `${this.API_URL}/driving-notifications`;

  acceptDrivingUrl(
    drivingNotificationId: number,
    accepted: boolean,
    userEmail: string
  ): string {
    return `${this.DRIVING_NOTIFICATIONS_URL}/update-status/${drivingNotificationId}/${accepted}/${userEmail}`;
  }

  ///////////////////LOCATION///////////////////

  LOCATIONS_URL = `${this.API_URL}/locations`;

  ///////////////////REPORT///////////////////

  REPORTS_URL = `${this.API_URL}/reports`;

  reportsForUserUrl(userId: number): string {
    return `${this.REPORTS_URL}/all-for-user/${userId}`;
  }

  ///////////////////REVIEW///////////////////

  REVIEWS_URL = `${this.API_URL}/reviews`;

  reviewsForDriverUrl(driverId: number): string {
    return `${this.REVIEWS_URL}/all-for-driver/${driverId}`;
  }

  deleteReviewUrl(id: number): string {
    return `${this.REVIEWS_URL}/${id}`;
  }

  reviewedDrivingsForUser(userId: number): string {
    return `${this.REVIEWS_URL}/reviewedDrivings/${userId}`;
  }

  ///////////////////ROUTE///////////////////

  ROUTES_URL = `${this.API_URL}/routes`;
  POSSIBLE_ROUTES_URL = `${this.ROUTES_URL}/possible`;

  routePathUrl(id: number): string {
    return `${this.ROUTES_URL}/path/${id}`;
  }

  ///////////////////DRIVER UPDATE APPROVAL/////

  DRIVER_UPDATE_APPROVAL = `${this.API_URL}/driver-update-approval`;

  rejectDriverRequest(id: number): string {
    return `${this.DRIVER_UPDATE_APPROVAL}/reject/${id}`;
  }

  approveDriverRequest(id: number): string {
    return `${this.DRIVER_UPDATE_APPROVAL}/approve/${id}`;
  }

  ///////////////////VEHICLE///////////////////

  VEHICLES_URL = `${this.API_URL}/vehicles`;
  ACTIVE_VEHICLES_URL = `${this.VEHICLES_URL}/active`;
  UPDATE_CURRENT_LOCATION_URL = `${this.VEHICLES_URL}/update-current-location`;

  deleteVehicleUrl(id: number): string {
    return `${this.VEHICLES_URL}/${id}`;
  }

  ratingForVehicleUrl(id: number): string {
    return `${this.VEHICLES_URL}/rating/${id}`;
  }

  vehicleByVehicleTypeUrl(vehicleType: string): string {
    return `${this.VEHICLES_URL}/${vehicleType}`;
  }

  vehicleByDriverId(driverId: string): string {
    return `${this.VEHICLES_URL}/vehicle-by-driver/${driverId}`;
  }

  ///////////////////VEHICLE TYPE INFOS///////////////////
  VEHICLE_TYPE_INFOS_URL = `${this.API_URL}/vehicle-type-infos`;

  priceForRouteAndVehicleUrl(vehicleType: string, kilometers: number): string {
    return `${this.VEHICLE_TYPE_INFOS_URL}/price/${vehicleType}/${kilometers}`;
  }

  averagePriceForRoute(kilometers: number): string {
    return `${this.VEHICLE_TYPE_INFOS_URL}/average-price/${kilometers}`;
  }

  ///////////////////VERIFY///////////////////

  VERIFY_URL = `${this.API_URL}/verify`;
  SEND_CODE_AGAIN_URL = `${this.VERIFY_URL}/send-code-again`;

  ///////////////////CHAT ROOM///////////////////

  CHAT_ROOMS_URL = `${this.API_URL}/chat-rooms`;
  RESOLVE_CHAT_URL = `${this.CHAT_ROOMS_URL}/resolve`;
  SET_MESSAGE_SEEN_URL = `${this.CHAT_ROOMS_URL}/seen-messages`;

  activeChatRoomsForUserUrl(userEmail: string): string {
    return `${this.CHAT_ROOMS_URL}/${userEmail}`;
  }

  allChatRoomsForUserUrl(userEmail: string): string {
    return `${this.CHAT_ROOMS_URL}/all/${userEmail}`;
  }

  ///////////////////PAYING INFO///////////////////

  PAYING_INFOS_URL = `${this.API_URL}/paying-infos`;

  payingInfoForUser(userId: number) {
    return `${this.PAYING_INFOS_URL}/${userId};`;
  }

  ///////////////////PAYPAL///////////////////

  PAYPAL_URL = `${this.API_URL}/paypal`;
  CREATE_PAYMENT_URL = `${this.PAYPAL_URL}/create-payment`;
  COMPLETE_PAYMENT_URL = `${this.PAYPAL_URL}/complete-payment`;

  ///////////////////TOKEN BANK///////////////////

  TOKEN_BANKS_URL = `${this.API_URL}/token-banks`;
  SPENDING_TOKENS_URL = `${this.TOKEN_BANKS_URL}/in-app-spending`;

  tokenBankForUser(userId: number): string {
    return `${this.TOKEN_BANKS_URL}/${userId}`;
  }

  ///////////////////NOTIFICATION BELL///////////////////
  NOTIFICATION_BELL_URL = `${this.API_URL}/bell-notifications`;

  bellNotificationsForUser(userId: number): string {
    return `${this.NOTIFICATION_BELL_URL}/${userId}`;
  }

  setAllNotificationsAsSeen(userId: number): string {
    return `${this.NOTIFICATION_BELL_URL}/seen/${userId}`;
  }

}
