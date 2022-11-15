import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../model/request/user/login-request';
import { LoginResponse } from '../model/response/user/login';
import { User } from '../model/response/user/user';
import { Route } from '../model/response/route';
import { ConfigService } from './config.service';
import { BehaviorSubject, Observable, Subject } from 'rxjs';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  currentUser$ = new BehaviorSubject<User>(null);
  ROLE_ADMIN: string = "ROLE_ADMIN";
  ROLE_REGULAR_USER: string = "ROLE_REGULAR_USER";
  ROLE_DRIVER: string = "ROLE_DRIVER";

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private router: Router
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
    if (user !== null && user !== undefined){

        this.currentUser$.next(JSON.parse(user));
    }else{

      this.currentUser$.next(null);
    }
    return this.currentUser$;
  }

  userIsAdmin(user: User): boolean {
    /*const userString = localStorage.getItem('user');
    if (userString !== null && userString !== undefined){
      const user = JSON.parse(userString);
      if (user.role.name === this.ROLE_ADMIN){

        return true;
      }
    }
    return false;*/

    return user.role.name === this.ROLE_ADMIN;
  }

  userIsRegular(user: User): boolean{
    return user.role.name === this.ROLE_REGULAR_USER;
  }

  userIsDriver(user: User): boolean{
    return user.role.name === this.ROLE_DRIVER;
  }

  tokenIsPresent() {
    let accessToken = this.getToken();
    return accessToken !== null;
  }

  getToken() {
    return localStorage.getItem('token');
  }

  getFavouriteRoutesForUser(email: string){
    return this.http.get<Route[]>(this.configService.get_favourite_routes(email))
  }
}
