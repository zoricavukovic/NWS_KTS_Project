import { ComponentFixture, TestBed, tick } from '@angular/core/testing';
import { Router } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { fakeAsync } from '@angular/core/testing';
import { BrowserModule, By } from '@angular/platform-browser';
import { BehaviorSubject, of } from 'rxjs';
import { RegistrationComponent } from './registration.component';
import { AuthService } from 'src/modules/auth/services/auth-service/auth.service';
import { UserService } from 'src/modules/shared/services/user-service/user.service';
import { RegistrationResponse } from 'src/modules/shared/models/user/registration-response';
import { ToastrService } from 'ngx-toastr';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { RegularUser } from 'src/modules/shared/models/user/regular-user';

describe('RegistrationComponent', () => {
  let componentForRegistration: RegistrationComponent;
  let fixture: ComponentFixture<RegistrationComponent>;
  const authServiceSpy = jasmine.createSpyObj('AuthService', [
    'userIsAdmin',
    'getSubjectCurrentUser',
  ]);

  const toastrServiceMock = jasmine.createSpyObj('ToastrService', [
    'error',
    'success',
  ]);

  const routerMock = {
    navigate: jasmine.createSpy('navigate'),
  };

  const userServiceMock = jasmine.createSpyObj('UserService', [
    'registerRegularUser',
  ]);

  authServiceSpy.getSubjectCurrentUser.and.returnValue(
    of(BehaviorSubject<null>)
  );
  authServiceSpy.userIsAdmin.and.returnValue(false);

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [RegistrationComponent],
      imports: [
        BrowserModule,
        FormsModule,
        ReactiveFormsModule,
        MatAutocompleteModule,
      ],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerMock },
        { provide: ToastrService, useValue: toastrServiceMock },
        { provide: UserService, useValue: userServiceMock },
      ],
    });

    fixture = TestBed.createComponent(RegistrationComponent);
    componentForRegistration = fixture.componentInstance;
  });

  it('return user null on init', fakeAsync(() => {
    componentForRegistration.ngOnInit();
    tick();
    fixture.detectChanges();
    expect(authServiceSpy.getSubjectCurrentUser).toHaveBeenCalled();
    expect(authServiceSpy.userIsAdmin).toHaveBeenCalled();
    tick();
    expect(componentForRegistration.showDriverForm).toBe(false);
  }));

  it('should return error when fields are empty', fakeAsync(() => {
    userServiceMock.registerRegularUser.calls.reset();
    componentForRegistration.registrationForm.patchValue({
      emailFormControl: '',
      passwordFormControl: '',
      passwordAgainFormControl: '',
      phoneNumberFormControl: '',
      nameFormControl: '',
      surnameFormControl: '',
      vehicleType: '',
      cityFormControl: '',
      babySeat: false,
      petFriendly: false,
    });

    fixture.detectChanges();
    expect(componentForRegistration.registrationForm.valid).toBeFalsy();

    const emailError = fixture.debugElement.query(
      By.css("mat-error[name='emptyEmail']")
    ).nativeElement;
    expect(emailError.textContent.trim()).toBe('Email is required');

    const phoneNumberError = fixture.debugElement.query(
      By.css("mat-error[name='emptyPhone']")
    ).nativeElement;
    expect(phoneNumberError.textContent.trim()).toBe(
      'Phone number is required'
    );

    const passwordError = fixture.debugElement.query(
      By.css("mat-error[name='emptyPassword']")
    ).nativeElement;
    expect(passwordError.textContent.trim()).toBe('Password is required');

    const passwordAgainError = fixture.debugElement.query(
      By.css("mat-error[name='emptyPasswordAgain']")
    ).nativeElement;
    expect(passwordAgainError.textContent.trim()).toBe(
      'Confirm password is required'
    );

    const nameError = fixture.debugElement.query(
      By.css("mat-error[name='emptyName']")
    ).nativeElement;
    expect(nameError.textContent.trim()).toBe('Name is required');

    const surnameError = fixture.debugElement.query(
      By.css("mat-error[name='emptySurname']")
    ).nativeElement;
    expect(surnameError.textContent.trim()).toBe('Surname is required');

    const cityError = fixture.debugElement.query(
      By.css("mat-error[name='emptyCity']")
    ).nativeElement;
    expect(cityError.textContent.trim()).toBe('City is required');
    componentForRegistration.register();
    expect(userServiceMock.registerRegularUser).toHaveBeenCalledTimes(0);
  }));

  it('should return error when fields are not valid', fakeAsync(() => {
    userServiceMock.registerRegularUser.calls.reset();
    for (const {
      emailFormControl,
      passwordFormControl,
      passwordAgainFormControl,
      phoneNumberFormControl,
      nameFormControl,
      surnameFormControl,
      cityFormControl,
      message,
    } of [
      {
        emailFormControl: 'email',
        passwordFormControl: 'sifra123@',
        passwordAgainFormControl: 'sifra123@',
        phoneNumberFormControl: '0674837484',
        nameFormControl: 'Ana',
        surnameFormControl: 'Anic',
        cityFormControl: 'Novi Sad',
        message: 'Please enter a valid email address',
      },
      {
        emailFormControl: 'ana@gmail.com',
        passwordFormControl: 'sifra',
        passwordAgainFormControl: 'sifra',
        phoneNumberFormControl: '0674837484',
        nameFormControl: 'Ana',
        surnameFormControl: 'Anic',
        cityFormControl: 'Novi Sad',
        message:
          'Password should contains at least 8 characters, one digit and one special character',
      },
      {
        emailFormControl: 'ana@gmail.com',
        passwordFormControl: 'sifra123@',
        passwordAgainFormControl: 'sifra123@',
        phoneNumberFormControl: '067',
        nameFormControl: 'Ana',
        surnameFormControl: 'Anic',
        cityFormControl: 'Novi Sad',
        message: 'Please enter a valid phone number (8-12 digits)',
      },
      {
        emailFormControl: 'ana@gmail.com',
        passwordFormControl: 'sifra123@',
        passwordAgainFormControl: 'sifra123@',
        phoneNumberFormControl: '0674837484',
        nameFormControl: '123',
        surnameFormControl: 'Anic',
        cityFormControl: 'Novi Sad',
        message: 'Please enter a valid name',
      },
      {
        emailFormControl: 'ana@gmail.com',
        passwordFormControl: 'sifra123@',
        passwordAgainFormControl: 'sifra123@',
        phoneNumberFormControl: '0674837484',
        nameFormControl: 'Ana',
        surnameFormControl: '123',
        cityFormControl: 'Novi Sad',
        message: 'Please enter a valid surname',
      },
      {
        emailFormControl: 'ana@gmail.com',
        passwordFormControl: 'sifra123@',
        passwordAgainFormControl: 'sifra123@',
        phoneNumberFormControl: '0674837484',
        nameFormControl: 'Ana',
        surnameFormControl: 'Anic',
        cityFormControl: '123',
        message: 'City cannot contain digits',
      },
    ]) {
      componentForRegistration.registrationForm.patchValue({
        emailFormControl: emailFormControl,
        passwordFormControl: passwordFormControl,
        passwordAgainFormControl: passwordAgainFormControl,
        phoneNumberFormControl: phoneNumberFormControl,
        nameFormControl: nameFormControl,
        surnameFormControl: surnameFormControl,
        vehicleType: '',
        cityFormControl: cityFormControl,
        babySeat: false,
        petFriendly: false,
      });

      fixture.detectChanges();
      expect(componentForRegistration.registrationForm.valid).toBeFalsy();

      const errorField = fixture.debugElement.query(
        By.css('mat-error')
      ).nativeElement;
      expect(errorField.textContent.trim()).toBe(message);
    }
  }));

  it('should return error when password are not match', fakeAsync(() => {
    toastrServiceMock.error.calls.reset();
    componentForRegistration.registrationForm.patchValue({
      emailFormControl: 'ana@gmail.com',
      passwordFormControl: 'sifra123@',
      passwordAgainFormControl: 'sifra12',
      phoneNumberFormControl: '0674837484',
      nameFormControl: 'Ana',
      surnameFormControl: 'Anic',
      cityFormControl: 'Novi Sad',
      vehicleType: '',
      babySeat: false,
      petFriendly: false,
    });

    fixture.detectChanges();
    expect(componentForRegistration.registrationForm.valid).toBeFalsy();
    componentForRegistration.register();
    tick();
    expect(toastrServiceMock.error).toHaveBeenCalledOnceWith(
      'Passwords not match'
    );
  }));

  it('should register regular user success', fakeAsync(() => {
    toastrServiceMock.error.calls.reset();
    componentForRegistration.registrationForm.patchValue({
      emailFormControl: 'ana@gmail.com',
      passwordFormControl: 'sifra123@',
      passwordAgainFormControl: 'sifra123@',
      phoneNumberFormControl: '0674837484',
      nameFormControl: 'Ana',
      surnameFormControl: 'Anic',
      cityFormControl: 'Novi Sad',
      vehicleType: '',
      babySeat: false,
      petFriendly: false,
    });

    const registrationRequest: RegularUser = {
      email: 'ana@gmail.com',
      name: 'Ana',
      surname: 'Anic',
      city: 'Novi Sad',
      phoneNumber: '0674837484',
      password: 'sifra123@',
      confirmPassword: 'sifra123@',
    };

    expect(componentForRegistration.registrationForm.valid).toBeTruthy();
    const registrationResponse: RegistrationResponse = {
      email: 'ana@gmail.com',
      verifyId: 1,
    };
    userServiceMock.registerRegularUser.and.returnValue(
      of(registrationResponse)
    );
    componentForRegistration.register();
    expect(userServiceMock.registerRegularUser).toHaveBeenCalledWith(
      registrationRequest
    );

    tick();
    expect(toastrServiceMock.success).toHaveBeenCalledWith(
      'Please go to ana@gmail.com to verify account!',
      'Registration successfully'
    );
    expect(routerMock.navigate).toHaveBeenCalledWith(['/verify/1']);
  }));
});
