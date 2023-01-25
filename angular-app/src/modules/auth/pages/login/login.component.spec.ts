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
  SocialAuthServiceConfig,
  SocialLoginModule,
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
});
