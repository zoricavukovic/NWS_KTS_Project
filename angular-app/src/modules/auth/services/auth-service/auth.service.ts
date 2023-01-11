import {HttpClient} from '@angular/common/http';
import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {Router} from '@angular/router';
import {User} from "../../../shared/models/user/user";
import {LoginRequest} from "../../../shared/models/user/login-request";
import {LoginResponse} from "../../../shared/models/user/login-response";
import {Route} from "../../../shared/models/route/route";
import {ConfigService} from "../../../shared/services/config-service/config.service";
import {WebSocketService} from "../../../shared/services/web-socket-service/web-socket.service";

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  public currentUser$: BehaviorSubject<User>;

  ROLE_ADMIN: string;
  ROLE_REGULAR_USER: string;
  ROLE_DRIVER: string;

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private router: Router,
    private chatService: WebSocketService
  ) {
    this.currentUser$ = new BehaviorSubject<User>(null);
    this.ROLE_ADMIN= 'ROLE_ADMIN';
    this.ROLE_REGULAR_USER = 'ROLE_REGULAR_USER';
    this.ROLE_DRIVER = 'ROLE_DRIVER';
  }

  login(loginRequest: LoginRequest): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      this.configService.LOGIN_URL,
      loginRequest
    );
  }

  loginWithGoogle(token: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      this.configService.GOOGLE_LOGIN_URL,
      { token }
    );
  }

  loginWithFacebook(token: string): Observable<LoginResponse> {
    return this.http.post<LoginResponse>(
      this.configService.FACEBOOK_LOGIN_URL,
      { token }
    );
  }

  setLocalStorage(loginResponse: LoginResponse): void {
    localStorage.setItem('token', loginResponse.token);
    localStorage.setItem('user', JSON.stringify(loginResponse.userDTO));
    localStorage.setItem('email', loginResponse.userDTO.email);
    this.currentUser$.next(loginResponse.userDTO);
  }

  logOut() {
    localStorage.clear();
    this.chatService.disconnect();
    this.currentUser$.next(null);
  }

  setOfflineStatus(): Observable<User> {
    return this.http.post<User>(
      this.configService.LOGOUT_URL,
      this.currentUser$?.value?.email
    );
  }

  getLoggedParsedUser(): User {
    const userString = localStorage.getItem('user');
    if (userString !== null && userString !== undefined) {
      return JSON.parse(userString);
    }

    return null;
  }

  userIsAdmin(): boolean {
    const user: User = this.getLoggedParsedUser();

    return user && user.role.name === this.ROLE_ADMIN;
  }

  userIsRegular(): boolean {
    const user: User = this.getLoggedParsedUser();

    return user && user.role.name === this.ROLE_REGULAR_USER;
  }

  userIsDriver(): boolean {
    const user: User = this.getLoggedParsedUser();

    return user && user.role.name === this.ROLE_DRIVER;
  }

  setUserInLocalStorage(user: User): void {
    localStorage.setItem('user', JSON.stringify(user));
    this.currentUser$.next(user);
  }

  getSubjectCurrentUser(): BehaviorSubject<User> {
    const user = localStorage.getItem('user');
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
      );
      this.currentUser$.next(currentUser);
    } else {
      this.currentUser$.next(null);
    }

    return this.currentUser$;
  }

  public get getCurrentUserId(): number {
    const user = localStorage.getItem('user');
    if (user !== null && user !== undefined) {
      const parsedUser: User = JSON.parse(user);
      return parsedUser.id;
    }
    return -1;
  }

  getFavouriteRoutesForUser(userId: number): Observable<Route[]> {
    return this.http.get<Route[]>(
      this.configService.allFavouriteRoutesUrl(userId)
    );
  }
}
