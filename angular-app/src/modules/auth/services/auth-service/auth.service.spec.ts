import {fakeAsync, getTestBed, TestBed, tick} from '@angular/core/testing';
import {AuthService} from "./auth.service";
import {HttpClientTestingModule, HttpTestingController} from "@angular/common/http/testing";
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {ConfigService} from "../../../shared/services/config-service/config.service";
import {LoginResponse} from "../../../shared/models/user/login-response";
import {LoginRequest} from "../../../shared/models/user/login-request";
import {NgxsModule, Store} from "@ngxs/store";
import {WebSocketService} from "../../../shared/services/web-socket-service/web-socket.service";
import {DrivingNotificationState} from "../../../shared/state/driving-notification.state";

describe('AuthService', () => {
  let injector;
  let authService: AuthService;
  let httpMock: HttpTestingController;
  let httpClient: HttpClient;
  let store: Store;
    const errorMessage = 'Email or password is not correct!';
  const unAuthorizesStatus = 401;

  const configServiceMock = jasmine.createSpyObj('ConfigService', ['getLoginUrl', 'getGoogleLoginUrl', 'getFacebookLoginUrl']);

  const webSocketServiceMock = jasmine.createSpy('WebSocketService');

  const mockLoginResponse: LoginResponse = {
    token: "token1231324213213",
    userDTO: {
      id: 1,
      email: "ana@gmail.com",
      name: "Ana",
      surname: "Ancic",
      phoneNumber: "012345678",
      city: "Novi Sad",
      role: {
        name: "ROLE_REGULAR_USER"
      },
      profilePicture: "defult-user.png",
      online: true
    }
  }

  beforeEach(() => {

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, NgxsModule.forRoot([DrivingNotificationState])],
      providers: [
        AuthService,
        {provide: ConfigService, useValue: configServiceMock},
        {provide: WebSocketService, useValue: webSocketServiceMock}
      ]
    });
    injector = getTestBed();
    authService = TestBed.inject(AuthService);
    httpClient = TestBed.inject(HttpClient);
    httpMock = TestBed.inject(HttpTestingController);
    store = TestBed.inject(Store);
  });

  afterEach(()=>{
    httpMock.verify();
  })

  it('login() should return logged user', fakeAsync(() => {
    const loginRequest: LoginRequest = {
      email: "ana@gmail.com",
      password: "sifra123@"
    }
    let loginResponse: LoginResponse;

    configServiceMock.getLoginUrl.and.returnValue('/auth/login');
    authService.login(loginRequest).subscribe(res => loginResponse = res);

    const req = httpMock.expectOne('/auth/login');
    expect(req.request.method).toBe('POST');
    req.flush(mockLoginResponse);

    tick();

    expect(loginResponse).toBeDefined();
    expect(loginResponse.token).toBe("token1231324213213");
    expect(loginResponse.userDTO.id).toBe(1);
    expect(loginResponse.userDTO.email).toBe("ana@gmail.com");
    expect(loginResponse.userDTO.name).toBe("Ana");
    expect(loginResponse.userDTO.surname).toBe("Ancic");
    expect(loginResponse.userDTO.phoneNumber).toBe("012345678");
    expect(loginResponse.userDTO.city).toBe("Novi Sad");
    expect(loginResponse.userDTO.role.name).toBe("ROLE_REGULAR_USER");
    expect(loginResponse.userDTO.profilePicture).toBe("defult-user.png");
    expect(loginResponse.userDTO.online).toBe(true);
  }));

  it('login() should return error message', fakeAsync(() => {
    for (const {email, password, status, error} of [
      {email: "", password: "sifra123@", status: 400, error: "Email must exist."},
      {email: "email", password: "sifra123@", status: 400, error: "Email is in wrong format."},
      {email: "ana@gmail.com", password: "sifra1234252", status: 400, error: "Password must contain at least 8 characters. At least one number and one special character."},
      {email: "ana@gmail.com", password: "not_exist123@", status: 401, error: "You are not authorized to perform this action"},
    ]) {
      const loginRequest: LoginRequest = {
        email: email,
        password: password
      }

      let loginResponse;
      const mockLoginResponse: HttpErrorResponse = new HttpErrorResponse({error: error, status: status});
      configServiceMock.getLoginUrl.and.returnValue('/auth/login');
      authService.login(loginRequest).subscribe(res => loginResponse = res);

      const req = httpMock.expectOne('/auth/login');
      expect(req.request.method).toBe('POST');
      req.flush(mockLoginResponse);

      tick();

      expect(loginResponse).toBeDefined();
      expect(loginResponse.error).toBe(error);
      expect(loginResponse.status).toBe(status);
    }
  }));


  it('loginWithGoogle should return logged user', fakeAsync(() => {
    const token = "token123"

    let loginResponse: LoginResponse;

    configServiceMock.getGoogleLoginUrl.and.returnValue('/auth/login/google');
    authService.loginWithGoogle(token).subscribe(res => loginResponse = res);

    const req = httpMock.expectOne('/auth/login/google');
    expect(req.request.method).toBe('POST');
    req.flush(mockLoginResponse);

    tick();

    expect(loginResponse).toBeDefined();
    expect(loginResponse.token).toBe("token1231324213213");
    expect(loginResponse.userDTO.id).toBe(1);
    expect(loginResponse.userDTO.email).toBe("ana@gmail.com");
    expect(loginResponse.userDTO.name).toBe("Ana");
    expect(loginResponse.userDTO.surname).toBe("Ancic");
    expect(loginResponse.userDTO.phoneNumber).toBe("012345678");
    expect(loginResponse.userDTO.city).toBe("Novi Sad");
    expect(loginResponse.userDTO.role.name).toBe("ROLE_REGULAR_USER");
    expect(loginResponse.userDTO.profilePicture).toBe("defult-user.png");
    expect(loginResponse.userDTO.online).toBe(true);
  }));

  it('loginWithGoogle should return error message', fakeAsync(() => {
      const token = "token123"

      let loginResponse;
      const mockLoginResponse: HttpErrorResponse = new HttpErrorResponse({error: errorMessage, status: unAuthorizesStatus});
      configServiceMock.getGoogleLoginUrl.and.returnValue('/auth/login/google');
      authService.loginWithGoogle(token).subscribe(res => loginResponse = res);

      const req = httpMock.expectOne('/auth/login/google');
      expect(req.request.method).toBe('POST');
      req.flush(mockLoginResponse);

      tick();

      expect(loginResponse).toBeDefined();
      expect(loginResponse.error).toBe(errorMessage);
      expect(loginResponse.status).toBe(unAuthorizesStatus);
    }
  ));

  it('loginWithFacebook should return logged user', fakeAsync(() => {
    const token = "token123"

    let loginResponse: LoginResponse;

    configServiceMock.getFacebookLoginUrl.and.returnValue('/auth/login/facebook');
    authService.loginWithFacebook(token).subscribe(res => loginResponse = res);

    const req = httpMock.expectOne('/auth/login/facebook');
    expect(req.request.method).toBe('POST');
    req.flush(mockLoginResponse);

    tick();

    expect(loginResponse).toBeDefined();
    expect(loginResponse.token).toBe("token1231324213213");
    expect(loginResponse.userDTO.id).toBe(1);
    expect(loginResponse.userDTO.email).toBe("ana@gmail.com");
    expect(loginResponse.userDTO.name).toBe("Ana");
    expect(loginResponse.userDTO.surname).toBe("Ancic");
    expect(loginResponse.userDTO.phoneNumber).toBe("012345678");
    expect(loginResponse.userDTO.city).toBe("Novi Sad");
    expect(loginResponse.userDTO.role.name).toBe("ROLE_REGULAR_USER");
    expect(loginResponse.userDTO.profilePicture).toBe("defult-user.png");
    expect(loginResponse.userDTO.online).toBe(true);
  }));

  it('loginWithGoogle should return error message', fakeAsync(() => {
      const token = "token123"

      let loginResponse;
      const mockLoginResponse: HttpErrorResponse = new HttpErrorResponse({error: errorMessage, status: unAuthorizesStatus});
      configServiceMock.getFacebookLoginUrl.and.returnValue('/auth/login/facebook');
      authService.loginWithFacebook(token).subscribe(res => loginResponse = res);

      const req = httpMock.expectOne('/auth/login/facebook');
      expect(req.request.method).toBe('POST');
      req.flush(mockLoginResponse);

      tick();

      expect(loginResponse).toBeDefined();
      expect(loginResponse.error).toBe(errorMessage);
      expect(loginResponse.status).toBe(unAuthorizesStatus);
    }
  ));


});
