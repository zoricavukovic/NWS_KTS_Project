import { ComponentFixture, TestBed, tick } from '@angular/core/testing';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { fakeAsync } from '@angular/core/testing';
import { BrowserModule, By } from '@angular/platform-browser';
import { of, throwError } from 'rxjs';
import { LoginComponent } from './login.component';
import { AuthService } from '../../services/auth-service/auth.service';
import {
  FacebookLoginProvider,
  GoogleLoginProvider,
  SocialAuthService,
  SocialAuthServiceConfig,
  SocialLoginModule,
  SocialUser,
} from '@abacritt/angularx-social-login';
import { environment } from '../../../../environments/environment';
import { ToastrService } from 'ngx-toastr';
import { WebSocketService } from '../../../shared/services/web-socket-service/web-socket.service';
import { LoginRequest } from '../../../shared/models/user/login-request';
import { HttpErrorResponse } from '@angular/common/http';
import { LoginResponse } from '../../../shared/models/user/login-response';

describe('LoginComponent', () => {
  let componentForLogin: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;

  const authServiceSpy = jasmine.createSpyObj('AuthService', [
    'login',
    'setLocalStorage',
    'loginWithGoogle',
    'loginWithFacebook'
  ]);

  const toastrServiceMock = jasmine.createSpyObj('ToastrService', [
    'error',
    'success',
  ]);
  const webSocketServiceMock = jasmine.createSpyObj('WebSocketService', [
    'connect',
  ]);

  const routerMock = {
    navigate: jasmine.createSpy('navigate'),
  };

  const errorMessage = 'Email or password is not correct!';

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        SocialLoginModule,
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerMock },
        { provide: ToastrService, useValue: toastrServiceMock },
        { provide: WebSocketService, useValue: webSocketServiceMock },
        {
          provide: 'SocialAuthServiceConfig',
          useValue: {
            autoLogin: false,
            providers: [
              {
                id: GoogleLoginProvider.PROVIDER_ID,
                provider: new GoogleLoginProvider(environment.clientId),
              },
              {
                id: FacebookLoginProvider.PROVIDER_ID,
                provider: new FacebookLoginProvider(environment.facebookAppId),
              },
            ],
            onError: err => {
              console.error(err);
            },
          } as SocialAuthServiceConfig,
        },
      ],
    });

    fixture = TestBed.createComponent(LoginComponent);
    componentForLogin = fixture.componentInstance;
  });

  it('should return error when field are empty', fakeAsync(() => {
    authServiceSpy.login.calls.reset();
    fixture.detectChanges();

    componentForLogin.loginForm.patchValue({
      email: '',
      password: '',
    });

    fixture.detectChanges();
    expect(componentForLogin.loginForm.valid).toBeFalsy();
    const errorField = fixture.debugElement.queryAll(By.css('mat-error'));
    const emailError = errorField[0].nativeElement;
    expect(emailError.textContent.trim()).toBe('Email is required');
    const passwordError = errorField[1].nativeElement;
    expect(passwordError.textContent.trim()).toBe('Password is required');
    componentForLogin.logIn();
    expect(authServiceSpy.login).toHaveBeenCalledTimes(0);
  }));

  it('should return error when email is not valid', fakeAsync(() => {
    authServiceSpy.login.calls.reset();
    fixture.detectChanges();

    componentForLogin.loginForm.patchValue({
      email: 'serbuber',
      password: 'sifra123@',
    });

    fixture.detectChanges();
    expect(componentForLogin.loginForm.valid).toBeFalsy();
    const errorField = fixture.debugElement.queryAll(By.css('mat-error'));
    const emailError = errorField[0].nativeElement;
    expect(emailError.textContent.trim()).toBe('Not a valid email');
    componentForLogin.logIn();
    expect(authServiceSpy.login).toHaveBeenCalledTimes(0);
  }));

  it('should return error when password is too short', fakeAsync(() => {
    authServiceSpy.login.calls.reset();
    fixture.detectChanges();

    componentForLogin.loginForm.patchValue({
      email: 'serbuber@gmail.com',
      password: 'sifr1',
    });

    fixture.detectChanges();
    expect(componentForLogin.loginForm.valid).toBeFalsy();
    const errorField = fixture.debugElement.queryAll(By.css('mat-error'));
    const passwordError = errorField[1].nativeElement;
    expect(passwordError.textContent.trim()).toBe(
      'Password should contains at least 8 characters'
    );

    componentForLogin.logIn();
    expect(authServiceSpy.login).toHaveBeenCalledTimes(0);
  }));

  it('should return error when password is weak', fakeAsync(() => {
    authServiceSpy.login.calls.reset();
    toastrServiceMock.error.calls.reset();
    fixture.detectChanges();

    const loginRequest: LoginRequest = {
      email: 'serbubernwtkts@gmail.com',
      password: 'sifra12312',
    };
    componentForLogin.loginForm.patchValue(loginRequest);

    fixture.detectChanges();
    expect(componentForLogin.loginForm.valid).toBeTruthy();

    authServiceSpy.login.and.returnValue(
      throwError(
        () =>
          new HttpErrorResponse({
            error:
              'Password must contain at least 8 characters. At least one number and one special character.',
            status: 400,
          })
      )
    );

    componentForLogin.logIn();

    expect(authServiceSpy.login).toHaveBeenCalledOnceWith(loginRequest);
    expect(toastrServiceMock.error).toHaveBeenCalledOnceWith(
      'Email or password is not correct!',
      'Login failed'
    );
  }));

  it('should return error when user does not exist', fakeAsync(() => {
    authServiceSpy.login.calls.reset();
    toastrServiceMock.error.calls.reset();
    fixture.detectChanges();

    const loginRequest: LoginRequest = {
      email: 'serbubernwtkts@gmail.com',
      password: 'sifra123@111',
    };
    componentForLogin.loginForm.patchValue(loginRequest);

    fixture.detectChanges();
    expect(componentForLogin.loginForm.valid).toBeTruthy();

    authServiceSpy.login.and.returnValue(
      throwError(
        () =>
          new HttpErrorResponse({
            error: 'You are not authorized to perform this action.',
            status: 401,
          })
      )
    );

    componentForLogin.logIn();

    expect(authServiceSpy.login).toHaveBeenCalledOnceWith(loginRequest);
    expect(toastrServiceMock.error).toHaveBeenCalledOnceWith(
      'Email or password is not correct!',
      'Login failed'
    );
  }));

  it('should success when user exist', fakeAsync(() => {
    authServiceSpy.login.calls.reset();
    routerMock.navigate.calls.reset();
    fixture.detectChanges();

    const loginRequest: LoginRequest = {
      email: 'ana@gmail.com',
      password: 'sifra123@',
    };
    componentForLogin.loginForm.patchValue(loginRequest);

    fixture.detectChanges();
    expect(componentForLogin.loginForm.valid).toBeTruthy();

    const mockLoginResponse: LoginResponse = {
      token: 'token1231324213213',
      userDTO: {
        id: 1,
        email: 'ana@gmail.com',
        name: 'Ana',
        surname: 'Ancic',
        phoneNumber: '012345678',
        city: 'Novi Sad',
        role: {
          name: 'ROLE_REGULAR_USER',
        },
        profilePicture: 'defult-user.png',
        online: true,
      },
    };

    authServiceSpy.login.and.returnValue(of(mockLoginResponse));

    componentForLogin.logIn();

    expect(authServiceSpy.login).toHaveBeenCalledWith(loginRequest);

    tick();

    expect(routerMock.navigate).toHaveBeenCalledWith([
      '/serb-uber/user/map-page-view/-1',
    ]);
  }));

  it('should log in with Google and navigate to map page', fakeAsync(() => {
    authServiceSpy.login.calls.reset();
    authServiceSpy.loginWithGoogle.calls.reset();
    authServiceSpy.setLocalStorage.calls.reset();
    routerMock.navigate.calls.reset();
    // const user = { idToken: 'token' };
    const loginResponse: LoginResponse = {
      token: 'token1231324213213',
      userDTO: {
        id: 1,
        email: 'ana@gmail.com',
        name: 'Ana',
        surname: 'Ancic',
        phoneNumber: '012345678',
        city: 'Novi Sad',
        role: {
          name: 'ROLE_REGULAR_USER',
        },
        profilePicture: 'defult-user.png',
        online: true,
      },
    };

    const user: SocialUser = {
      provider: 'GOOGLE',
      id: '1',
      email: 'serbuber2@gmail.com',
      name: 'Test',
      photoUrl: 'Test',
      firstName: 'Test',
      lastName: 'Test',
      authToken: '1',
      idToken: 'some-id-token',
      authorizationCode: 'some',
      response: ''
    }


    authServiceSpy.loginWithGoogle.and.returnValue(of(loginResponse));

    const socialAuthService = TestBed.get(SocialAuthService);
    spyOn(socialAuthService, 'authState').and.returnValue(of(user));

    componentForLogin.ngOnInit();

    tick();

    authServiceSpy.loginWithGoogle(user.idToken).subscribe({
      next(loggedUser): void {
        authServiceSpy.setLocalStorage(loggedUser);
        routerMock.navigate(['/serb-uber/user/map-page-view/-1'])
      },
      error(): void {
        fail('Login with Google failed');
      }
    });

    expect(authServiceSpy.setLocalStorage).toHaveBeenCalledWith(loginResponse);
    expect(routerMock.navigate).toHaveBeenCalledWith(['/serb-uber/user/map-page-view/-1']);

  }));

  it('should handle error on login with Google', fakeAsync(() => {
    authServiceSpy.loginWithGoogle.calls.reset();
    routerMock.navigate.calls.reset();
    toastrServiceMock.error.calls.reset();

    const user: SocialUser = {
      provider: 'GOOGLE',
      id: '1',
      email: 'serbuber2@gmail.com',
      name: 'Test',
      photoUrl: 'Test',
      firstName: 'Test',
      lastName: 'Test',
      authToken: '1',
      idToken: 'some-id-token',
      authorizationCode: 'some',
      response: ''
    }

    const socialAuthService = TestBed.get(SocialAuthService);
    spyOn(socialAuthService, 'authState').and.returnValue(of(user));
    authServiceSpy.loginWithGoogle.and.returnValue(throwError({errorMessage}));

    componentForLogin.ngOnInit();

    tick();

    authServiceSpy.loginWithGoogle(user.idToken).subscribe({
      next(): void {
        fail('Login with Google should have been failed!')
      },
      error(error): void {
        toastrServiceMock.error(error.errorMessage, 'Login failed');
      }
    });

    expect(toastrServiceMock.error).toHaveBeenCalledWith('Email or password is not correct!', 'Login failed');
  }));


  it('should navigate to map page and connect to chat when login with facebook is successful', fakeAsync(() => {
    authServiceSpy.login.calls.reset();
    authServiceSpy.loginWithGoogle.calls.reset();
    authServiceSpy.setLocalStorage.calls.reset();
    routerMock.navigate.calls.reset();

    const data = {authToken: 'token'}
    const socialAuthService = TestBed.get(SocialAuthService);
    authServiceSpy.loginWithFacebook.and.returnValue(of({} as LoginResponse));
    spyOn(socialAuthService, 'signIn').and.returnValue(Promise.resolve({ data }));

    componentForLogin.signInWithFB();

    tick();

    authServiceSpy.loginWithFacebook(data.authToken).subscribe({
      next(): void {
        authServiceSpy.setLocalStorage({});
        webSocketServiceMock.connect();
        routerMock.navigate(['/serb-uber/user/map-page-view/-1'])
      },
      error(error): void {
        toastrServiceMock.error(error.errorMessage, 'Login failed');
      }
    });

    expect(socialAuthService.signIn).toHaveBeenCalledWith(FacebookLoginProvider.PROVIDER_ID);
    expect(authServiceSpy.loginWithFacebook).toHaveBeenCalledWith('token');
    expect(webSocketServiceMock.connect).toHaveBeenCalled();
    expect(authServiceSpy.setLocalStorage).toHaveBeenCalledWith({});
    expect(routerMock.navigate).toHaveBeenCalledWith(['/serb-uber/user/map-page-view/-1']);
  }));

  it('should navigate to map page and connect to chat when login with facebook is successful', fakeAsync(() => {
    authServiceSpy.login.calls.reset();
    authServiceSpy.loginWithGoogle.calls.reset();
    authServiceSpy.setLocalStorage.calls.reset();
    routerMock.navigate.calls.reset();

    const data = {authToken: 'token'}
    const socialAuthService = TestBed.get(SocialAuthService);
    authServiceSpy.loginWithFacebook.and.returnValue(throwError({errorMessage}));
    spyOn(socialAuthService, 'signIn').and.returnValue(Promise.resolve({ data }));

    componentForLogin.signInWithFB();

    tick();

    authServiceSpy.loginWithFacebook(data.authToken).subscribe({
      next(): void {
        //fail
      },
      error(error): void {
        toastrServiceMock.error(error.errorMessage, 'Login failed');
      }
    });

    expect(socialAuthService.signIn).toHaveBeenCalledWith(FacebookLoginProvider.PROVIDER_ID);
    expect(authServiceSpy.loginWithFacebook).toHaveBeenCalledWith('token');
    expect(toastrServiceMock.error).toHaveBeenCalledWith('Email or password is not correct!', 'Login failed');
  }));

});
