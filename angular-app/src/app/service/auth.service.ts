import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../model/user/login-request';
import { LoginResponse } from '../model/user/login-response';
import { User } from '../model/user/user';
import { Route } from '../model/route/route';
import { ConfigService } from './config.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { WebSocketService } from './web-socket.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public currentUser$: BehaviorSubject<User>;

  ROLE_ADMIN: string = 'ROLE_ADMIN';
  ROLE_REGULAR_USER: string = 'ROLE_REGULAR_USER';
  ROLE_DRIVER: string = 'ROLE_DRIVER';

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private router: Router,
    private chatService: WebSocketService
  ) {
    this.currentUser$ = new BehaviorSubject<User>(null);
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      this.configService.login_url,
      loginRequest
    );
  }

  loginWithGoogle(token: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      this.configService.login_with_gmail_url,
      { token }
    );
  }

  loginWithFacebook(token: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      this.configService.login_with_facebook_url,
      { token }
    );
  }

  setLocalStorage(loginResponse: LoginResponse): void {
    localStorage.setItem('token', 'Bearer ' + loginResponse.token);
    localStorage.setItem('user', JSON.stringify(loginResponse.userDTO));
    localStorage.setItem('email', loginResponse.userDTO.email);
    this.currentUser$.next(loginResponse.userDTO);
  }

  logOut() {
    this.chatService.disconnect();
    this.currentUser$.next(null);
    localStorage.clear();
  }

  setOfflineStatus(): Observable<User> {
    return this.http.post<User>(
      this.configService.logout_url,
      this.currentUser$?.value?.email,
      { headers: this.configService.getHeader() }
    );
  }

  getLoggedParsedUser(): User {
    const userString = localStorage.getItem('user');
    if (userString !== null && userString !== undefined){
      const user: User = JSON.parse(userString);

      return user;
    }

    return null;
  }

  userIsAdmin(): boolean {
    const user: User = this.getLoggedParsedUser();
    if (user && user.role.name === this.ROLE_ADMIN) {

      return true;
    }

    return false;
  }

  userIsRegular(): boolean {
    const user: User = this.getLoggedParsedUser();
    if (user && user.role.name === this.ROLE_REGULAR_USER) {

      return true;
    }

    return false;
  }

  userIsDriver(): boolean {
    const user: User = this.getLoggedParsedUser();
    if (user && user.role.name === this.ROLE_DRIVER) {

      return true;
    }

    return false;
  }

  setUserInLocalStorage(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUser$.next(user);
  }

  getSubjectCurrentUser(): BehaviorSubject<User> {
    let user = localStorage.getItem('user');
    if (user !== null && user !== undefined) {
        const parsedUser: User = JSON.parse(user);
        const currentUser: User = new User(
            parsedUser.id,
            parsedUser.email,
            parsedUser.name,
            parsedUser.surname,
            parsedUser.phoneNumber,
            parsedUser.city,
            parsedUser.role,
            parsedUser.profilePicture
          )
        this.currentUser$.next(currentUser);
    } else {

      this.currentUser$.next(null);
    }

    return this.currentUser$;
  }

  // public get getCurrentUser(): User {
  //   const user = localStorage.getItem('user');
  //   if (user !== null && user !== undefined) {
  //     const parsedUser: User = JSON.parse(user);
  //     this.currentUserSubject$.next(
  //       new User(
  //         parsedUser.id,
  //         parsedUser.email,
  //         parsedUser.name,
  //         parsedUser.surname,
  //         parsedUser.phoneNumber,
  //         parsedUser.city,
  //         parsedUser.role,
  //         parsedUser.profilePicture
  //       )
  //     );
  //   } else {
  //     this.currentUserSubject$.next(null);
  //   }
  //   return this.currentUserSubject$.value;
  // }

  public get getCurrentUserId(): number {
    const user = localStorage.getItem('user');
    if (user !== null && user !== undefined) {
      const parsedUser: User = JSON.parse(user);
      return parsedUser.id;
    }
    return -1;
  }

  tokenIsPresent() {
    const accessToken = this.getToken();
    return accessToken !== null;
  }

  getToken() {
    return localStorage.getItem('token');
  }

  getFavouriteRoutesForUser(userId: number) {
    return this.http.get<Route[]>(
      this.configService.get_favourite_routes(userId),
      { headers: this.configService.getHeader() }
    );
  }
}
