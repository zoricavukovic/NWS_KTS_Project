import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormControl,
  FormGroup,
  FormGroupDirective,
  NgForm,
  Validators,
} from '@angular/forms';
import { ErrorStateMatcher } from '@angular/material/core';
import { Observable, Subscription } from 'rxjs';
import { map, startWith } from 'rxjs/operators';
import { matchPasswordsValidator } from './confirm-password.validator';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { Vehicle } from '../../../shared/models/vehicle/vehicle';
import { Driver } from '../../../shared/models/user/driver';
import { RegularUser } from '../../../shared/models/user/regular-user';
import { UserService } from '../../../shared/services/user-service/user.service';
import { AuthService } from '../../../auth/services/auth-service/auth.service';

@Component({
  selector: 'app-registration',
  styleUrls: ['./registration.component.css'],
  templateUrl: './registration.component.html',
})
export class RegistrationComponent implements OnInit, OnDestroy {
  filteredCities: Observable<string[]>;
  registrationForm = new FormGroup(
    {
      emailFormControl: new FormControl('', [
        Validators.required,
        Validators.email,
      ]),
      phoneNumberFormControl: new FormControl('', [
        Validators.required,
        Validators.pattern('[0-9]{8,12}'),
      ]),
      nameFormControl: new FormControl('', [
        Validators.required,
        Validators.pattern('[a-zA-Z ]*'),
      ]),
      surnameFormControl: new FormControl('', [
        Validators.required,
        Validators.pattern('[a-zA-Z ]*'),
      ]),
      passwordAgainFormControl: new FormControl('', [
        Validators.required,
        Validators.minLength(8),
      ]),
      passwordFormControl: new FormControl('', [
        Validators.required,
        Validators.minLength(9),
      ]),
      cityFormControl: new FormControl('', [
        Validators.required,
        Validators.pattern('[a-zA-Z ]*'),
      ]),
      babySeat: new FormControl(false),
      petFriendly: new FormControl(false),
      vehicleType: new FormControl(''),
    },
    [matchPasswordsValidator()]
  );

  matcher = new MyErrorStateMatcher();
  cities: string[] = [
    'Beograd',
    'Novi Sad',
    'Kraljevo',
    'Kragujevac',
    'Jagodina',
    'Mladenovac',
    'Subotica',
    'Ruma',
    'Priboj',
    'Sabac',
    'Leskovac',
    'Vranje',
    'Smederevo',
    'Pozarevac',
    'Zrenjanin',
    'Sombor',
  ];
  registrationSubscription: Subscription;

  submitted = false;

  showDriverForm = false;
  hidePassword = true;
  hideConfirmPassword = true;

  petFriendly = false;
  babySeat = false;
  vehicleType: string;
  authSubscription: Subscription;

  ngOnInit(): void {
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.showDriverForm = this.authService.userIsAdmin();
        if (!user) {
          this.removeUnnecessaryValidators();
        }
      });
  }

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private toast: ToastrService,
    private router: Router
  ) {
    this.filteredCities = this.registrationForm
      .get('cityFormControl')
      .valueChanges.pipe(
        startWith(''),
        map(city => (city ? this._filterCities(city) : this.cities.slice()))
      );
  }

  _filterCities(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.cities.filter(city => city.toLowerCase().includes(filterValue));
  }

  getError() {
    return this.registrationForm.hasError('mismatch');
  }

  register() {
    if (this.registrationForm.hasError('mismatch')) {
      this.toast.error('Passwords not match');
    } else {
      if (!this.registrationForm.invalid) {
        if (this.showDriverForm) {
          const vehicle: Vehicle = {
            petFriendly: this.registrationForm.get('petFriendly').value,
            babySeat: this.registrationForm.get('babySeat').value,
            vehicleType: this.registrationForm.get('vehicleType').value,
          };
          const driver: Driver = {
            email: this.registrationForm.get('emailFormControl').value,
            password: this.registrationForm.get('passwordFormControl').value,
            confirmPassword: this.registrationForm.get(
              'passwordAgainFormControl'
            ).value,
            name: this.registrationForm.get('nameFormControl').value,
            surname: this.registrationForm.get('surnameFormControl').value,
            phoneNumber: this.registrationForm.get('phoneNumberFormControl')
              .value,
            city: this.registrationForm.get('cityFormControl').value,
            vehicle: vehicle,
          };
          this.registrationSubscription = this.userService
            .registerDriver(driver)
            .subscribe(
              res => {
                this.toast.success(
                  'Driver is created and waiting for verification!',
                  'Registration successfully'
                );
                console.log(res);
                this.router.navigate(['/serb-uber/user/map-page-view/-1']);
              },
              error => this.toast.error(error.error, 'Registration failed')
            );
        } else {
          const regularUser: RegularUser = {
            email: this.registrationForm.get('emailFormControl').value,
            password: this.registrationForm.get('passwordFormControl').value,
            confirmPassword: this.registrationForm.get(
              'passwordAgainFormControl'
            ).value,
            name: this.registrationForm.get('nameFormControl').value,
            surname: this.registrationForm.get('surnameFormControl').value,
            phoneNumber: this.registrationForm.get('phoneNumberFormControl')
              .value,
            city: this.registrationForm.get('cityFormControl').value,
          };
          this.registrationSubscription = this.userService
            .registerRegularUser(regularUser)
            .subscribe(
              response => {
                this.toast.success(
                  'Please go to ' + response.email + ' to verify account!',
                  'Registration successfully'
                );
                this.router.navigate([`/verify/${response.verifyId}`]);
              },
              error => this.toast.error(error.error, 'Registration failed')
            );
        }
      } else {
        this.toast.error(
          'Form is invalid. Check all fields.',
          'Registration failed'
        );
      }
    }
  }

  ngOnDestroy(): void {
    if (this.registrationSubscription) {
      this.registrationSubscription.unsubscribe();
    }

    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

  private removeUnnecessaryValidators() {
    this.registrationForm.get('babySeat').setValidators(null);
    this.registrationForm.get('vehicleType').setValidators(null);
    this.registrationForm.get('petFriendly').setValidators(null);
  }
}

export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(
    control: FormControl | null,
    form: FormGroupDirective | NgForm | null
  ): boolean {
    const isSubmitted = form && form.submitted;
    return !!(
      control &&
      control.invalid &&
      (control.dirty || control.touched || isSubmitted)
    );
  }
}
