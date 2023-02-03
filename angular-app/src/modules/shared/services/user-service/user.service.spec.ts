import { fakeAsync, getTestBed, TestBed, tick } from '@angular/core/testing';
import {
  HttpClientTestingModule,
  HttpTestingController,
} from '@angular/common/http/testing';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { ConfigService } from '../config-service/config.service';
import { UserService } from './user.service';
import { RegistrationResponse } from '../../models/user/registration-response';
import { RegularUser } from '../../models/user/regular-user';

describe('UserService', () => {
  let injector;
  let httpMock: HttpTestingController;
  let userService: UserService;
  let httpClient: HttpClient;
  const configServiceMock = jasmine.createSpyObj('ConfigService', [
    'getCreateRegularUserUrl',
    'getCreateDriverUrl',
  ]);

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [
        UserService,
        { provide: ConfigService, useValue: configServiceMock },
      ],
    });
    injector = getTestBed();
    httpClient = TestBed.inject(HttpClient);
    userService = TestBed.inject(UserService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('registerRegularUser() should return registration response', fakeAsync(() => {
    const registrationRequest: RegularUser = {
      email: 'ana@gmail.com',
      name: 'Ana',
      surname: 'Anic',
      city: 'Novi Sad',
      phoneNumber: '0674837484',
      password: 'sifra123@',
      confirmPassword: 'sifra123@',
    };

    const mockRegistrationResponse: RegistrationResponse = {
      email: 'ana@gmail.com',
      verifyId: 1,
    };

    let registrationResponse: RegistrationResponse;
    configServiceMock.getCreateRegularUserUrl.and.returnValue(
      '/users/create/regular-user'
    );
    userService
      .registerRegularUser(registrationRequest)
      .subscribe(res => (registrationResponse = res));

    const req = httpMock.expectOne('/users/create/regular-user');
    expect(req.request.method).toBe('POST');
    req.flush(mockRegistrationResponse);

    tick();

    expect(registrationResponse).toBeDefined();
    expect(registrationResponse.email).toBe('ana@gmail.com');
    expect(registrationResponse.verifyId).toBe(1);
  }));

  it('registerRegularUser() should return error message', fakeAsync(() => {
    const registrationRequest: RegularUser = {
      email: 'ana@gmail.com',
      name: 'Ana',
      surname: 'Anic',
      city: 'Novi Sad',
      phoneNumber: '0674837484',
      password: 'sifra123@',
      confirmPassword: 'sifra123@',
    };

    const mockErrorResponse: HttpErrorResponse = new HttpErrorResponse({
      error: 'User with email ana@gmail.com already exists.',
      status: 400,
    });

    let registrationResponse: RegistrationResponse;
    configServiceMock.getCreateRegularUserUrl.and.returnValue(
      '/users/create/regular-user'
    );
    userService
      .registerRegularUser(registrationRequest)
      .subscribe(res => (registrationResponse = res));

    const req = httpMock.expectOne('/users/create/regular-user');
    expect(req.request.method).toBe('POST');
    req.flush(mockErrorResponse);

    expect(mockErrorResponse).toBeDefined();
    expect(mockErrorResponse.error).toBe(
      'User with email ana@gmail.com already exists.'
    );
    expect(mockErrorResponse.status).toBe(400);
  }));
});
