import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../model/request/user/login-request';
import { LoginResponse } from '../model/response/user/login';
import { User } from '../model/response/user/user';
import { Route } from '../model/response/route';
import { ConfigService } from './config.service';
import { BehaviorSubject, Observable } from 'rxjs';
import { Router } from '@angular/router';
import { ChatService } from './chat.service';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  currentUser$ = new BehaviorSubject<User>(null);

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private router: Router,
    private chatService: ChatService
  ) {}

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
  }

  logOut() {
    this.chatService.disconnect();
    this.currentUser$.next(null);
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  setOfflineStatus(loggedUser: User): Observable<User> {
    return this.http.post<User>(
      this.configService.logout_url,
      loggedUser.email,
      {headers: this.configService.header}
    );
  }

  setUserInLocalStorage(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUser$.next(user);
  }

  getCurrentUser(): BehaviorSubject<User> {
    const user = localStorage.getItem('user');
    if (user !== null && user !== undefined) {
      const parsedUser: User = JSON.parse(user);
      this.currentUser$.next(
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
      this.currentUser$.next(null);
    }
    return this.currentUser$;
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
      this.configService.get_favourite_routes(userId)
    );
  }
}
