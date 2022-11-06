import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { LoginRequest } from '../model/login-request';
import { LoginResponse } from '../model/login-response';
import { User } from '../model/user';
import { ConfigService } from './config.service';
import { map } from 'rxjs/operators';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  currentUser: User;
  roleAdmin: String = "ROLE_ADMIN";

  constructor(
    private http: HttpClient, 
    private configService: ConfigService
  ) { }

    login(loginRequest: LoginRequest) {
      console.log(loginRequest);

      return this.http.post<LoginResponse>(this.configService.login_url, loginRequest)
      .pipe(
          map((response) => { 
              const loggedUser = response as LoginResponse;
              this.setLocalStorage(loggedUser); 
              this.currentUser = loggedUser.userDTO;

              return loggedUser.userDTO;
            }
          )
      );
    }

  loginWithGoogle(token:String):Observable<User> {
    
    return this.http.post<LoginResponse>(this.configService.login_with_gmail_url, {token})
    .pipe(
        map((response) => { 
            const loggedUser = response as LoginResponse;
            this.setLocalStorage(loggedUser); 
            this.currentUser = loggedUser.userDTO;

            return loggedUser.userDTO;
          }
        )
    );
  }

  loginWithFacebook(token:String):Observable<User> {
    
    return this.http.post<LoginResponse>(this.configService.login_with_facebook_url, {token})
    .pipe(
        map((response) => { 
            const loggedUser = response as LoginResponse;
            this.setLocalStorage(loggedUser); 
            this.currentUser = loggedUser.userDTO;

            return loggedUser.userDTO;
          }
        )
    );
  }

  setLocalStorage(loginResponse: LoginResponse): void {
    localStorage.setItem('token','Bearer ' + loginResponse.token);
    localStorage.setItem('user', JSON.stringify(loginResponse.userDTO));
  }

  setUSerInLocalStorage(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
  }

  logout(){
    localStorage.clear();
  }

  getCurrentUser(){
    let user = localStorage.getItem('user');
    if (user !== null){

        return JSON.parse(user); 
    }   

    return null;
  }

  userIsAdmin(): boolean{
    const userString = localStorage.getItem('user');
    if (userString !== null){
      const user = JSON.parse(userString);
      if (user.role.name === this.roleAdmin){

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
