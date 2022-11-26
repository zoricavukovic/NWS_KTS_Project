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
import { UserService } from 'src/app/service/user.service';
import { RegularUser } from 'src/app/model/user/regular-user';
import { matchPasswordsValidator } from './confirm-password.validator';
import { AuthService } from 'src/app/service/auth.service';
import { Driver } from 'src/app/model/user/driver';
import { Vehicle } from 'src/app/model/vehicle/vehicle';
import { isFormValid } from 'src/app/util/validation-function';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';

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
    },
    [matchPasswordsValidator()]
  );

  matcher = new MyErrorStateMatcher();
  cities: string[] = ['Belgrade', 'Novi Sad', 'Kraljevo', 'Sabac'];
  registrationSubscription: Subscription;
  currentUserSubscription: Subscription;

  submitted = false;

  showDriverForm = false;
  hidePassword = true;
  hideConfirmPassword = true;

  petFriendly = false;
  babySeat = false;
  vehicleType: string;

  ngOnInit(): void {
    this.showDriverForm = this.authService.getCurrentUser?.isUserAdmin();
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
    }
    if (isFormValid(this.registrationForm)) {
      if (this.showDriverForm) {
        const vehicle: Vehicle = {
          petFriendly: this.petFriendly,
          babySeat: this.babySeat,
          vehicleType: this.vehicleType,
        };
        const driver: Driver = {
          email: this.registrationForm.get('emailFormControl').value,
          password: this.registrationForm.get('passwordFormControl').value,
          confirmPassword: this.registrationForm.get('passwordAgainFormControl')
            .value,
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
                'Please go to ' + res.email + ' to verify account!',
                'Registration successfully'
              );
              this.router.navigate(['/map-view/-1']);
            },
            error => this.toast.error(error.error, 'Registration failed')
          );
      } else {
        const regularUser: RegularUser = {
          email: this.registrationForm.get('emailFormControl').value,
          password: this.registrationForm.get('passwordFormControl').value,
          confirmPassword: this.registrationForm.get('passwordAgainFormControl')
            .value,
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
    }
  }

  ngOnDestroy(): void {
    if (this.registrationSubscription) {
      this.registrationSubscription.unsubscribe();
    }
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
