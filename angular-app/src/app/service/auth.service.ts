import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../model/request/user/login-request';
import { LoginResponse } from '../model/response/user/login';
import { User } from '../model/response/user/user';
import { ConfigService } from './config.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Router } from '@angular/router';
import { ChatService } from './chat.service';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  currentUser$ = new BehaviorSubject<User>(null);
  ROLE_ADMIN: string = "ROLE_ADMIN";

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private router: Router,
    private chatService: ChatService
  ) { }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {

    return this.http.post<LoginResponse>(this.configService.login_url, loginRequest);
  }

  loginWithGoogle(token:String): Observable<LoginResponse> {

    return this.http.post<LoginResponse>(this.configService.login_with_gmail_url, {token});
  }

  loginWithFacebook(token:String): Observable<LoginResponse> {

    return this.http.post<LoginResponse>(this.configService.login_with_facebook_url, {token});
  }

  setLocalStorage(loginResponse: LoginResponse): void {
    localStorage.setItem('token','Bearer ' + loginResponse.token);
    localStorage.setItem('user', JSON.stringify(loginResponse.userDTO));
  }

  logOut(){
    this.chatService.disconnect();
    this.currentUser$.next(null);
    localStorage.clear();
    this.router.navigate(['/login']);
  }

  setUserInLocalStorage(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUser$.next(user);
  }

  getCurrentUser(): BehaviorSubject<User> {
    let user = localStorage.getItem('user');
    console.log(user);
    if (user !== null && user !== undefined){

        this.currentUser$.next(JSON.parse(user));
    }else{

      this.currentUser$.next(null);
    }
    return this.currentUser$;
  }

  userIsAdmin(): boolean {
    const userString = localStorage.getItem('user');
    if (userString !== null && userString !== undefined){
      const user = JSON.parse(userString);
      if (user.role.name === this.ROLE_ADMIN){

        return true;
      }
    }

    return false;
  }

  tokenIsPresent() {
    let accessToken = this.getToken();
    return accessToken !== null;
  }

  getToken() {
    return localStorage.getItem('token');
  }
}
