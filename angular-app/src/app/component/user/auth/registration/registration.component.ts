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
import { RegistrationRequest } from 'src/app/model/request/user/registration-request';
import { matchPasswordsValidator } from './confirm-password.validator';
import { AuthService } from 'src/app/service/auth.service';
import { Driver } from 'src/app/model/request/user/driver-registration-request';
import { VehicleRequest } from 'src/app/model/request/vehicle-request';
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
    this.currentUserSubscription = this.authService
      .getCurrentUser()
      .subscribe(user => {
        this.showDriverForm = user.isUserAdmin();
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
    }
    if (isFormValid(this.registrationForm)) {
      if (this.showDriverForm) {
        this.registrationSubscription = this.userService
          .registerDriver(
            new DriverRegistrationRequest(
              this.registrationForm.get('emailFormControl').value,
              this.registrationForm.get('passwordFormControl').value,
              this.registrationForm.get('passwordAgainFormControl').value,
              this.registrationForm.get('nameFormControl').value,
              this.registrationForm.get('surnameFormControl').value,
              this.registrationForm.get('phoneNumberFormControl').value,
              this.registrationForm.get('cityFormControl').value,
              new VehicleRequest(
                this.petFriendly,
                this.babySeat,
                this.vehicleType
              )
            )
          )
          .subscribe(
            res => {
              this.toast.success(
                'Please go to ' + res.email + ' to verify account!',
                'Registration successfully'
              );
              this.router.navigate(['/home-page']);
            },
            error => this.toast.error(error.error, 'Registration failed')
          );
      } else {
        this.registrationSubscription = this.userService
          .registerRegularUser(
            new RegistrationRequest(
              this.registrationForm.get('emailFormControl').value,
              this.registrationForm.get('passwordFormControl').value,
              this.registrationForm.get('passwordAgainFormControl').value,
              this.registrationForm.get('nameFormControl').value,
              this.registrationForm.get('surnameFormControl').value,
              this.registrationForm.get('phoneNumberFormControl').value,
              this.registrationForm.get('cityFormControl').value
            )
          )
          .subscribe(
            response => {
              console.log(response);
              this.toast.success(
                'You become new member of SerbUber',
                'Registration successfully'
              );
              this.router.navigate(['/login']);
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
