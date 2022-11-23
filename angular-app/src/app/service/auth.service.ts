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
  public currentUserSubject$: BehaviorSubject<User>;
  public currentUser: Observable<User>;

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private router: Router,
    private chatService: WebSocketService
  ) {
    this.currentUserSubject$ = new BehaviorSubject<User>(
      JSON.parse(localStorage.getItem('user'))
    );
    this.currentUser = this.currentUserSubject$.asObservable();
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
  }

  logOut() {
    this.chatService.disconnect();
    this.currentUserSubject$.next(null);
    localStorage.clear();
  }

  setOfflineStatus(): Observable<User> {
    return this.http.post<User>(
      this.configService.logout_url,
      this.getCurrentUser?.email,
      { headers: this.configService.getHeader() }
    );
  }

  setUserInLocalStorage(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUserSubject$.next(user);
  }

  public get getCurrentUser(): User {
    const user = localStorage.getItem('user');
    if (user !== null && user !== undefined) {
      const parsedUser: User = JSON.parse(user);
      this.currentUserSubject$.next(
        new User(
          parsedUser.id,
          parsedUser.email,
          parsedUser.name,
          parsedUser.surname,
          parsedUser.phoneNumber,
          parsedUser.city,
          parsedUser.role,
          parsedUser.profilePicture
        )
      );
    } else {
      this.currentUserSubject$.next(null);
    }
    return this.currentUserSubject$.value;
  }

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
