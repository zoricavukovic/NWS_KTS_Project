import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, NgForm, Validators, ValidatorFn, ValidationErrors, AbstractControl } from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import { Observable, Subscription } from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import { UserService } from 'src/app/service/user.service';
import { RegistrationRequest } from 'src/app/model/request/user/registration-request';
import { matchPasswordsValidator } from './confirm-password.validator';
import { AuthService } from 'src/app/service/auth.service';
import { DriverRegistrationRequest } from 'src/app/model/request/user/driver-registration-request';
import { VehicleRequest } from 'src/app/model/request/vehicle-request';
import { isFormValid } from 'src/app/util/validation-function';
import { NgToastService } from 'ng-angular-popup';
import { Router } from '@angular/router';


@Component({
  selector: 'app-registration',
  styleUrls: ['./registration.component.css'],
  templateUrl:'./registration.component.html'
})
export class RegistrationComponent implements OnInit, OnDestroy{

  filteredCities: Observable<string[]>;
  registrationForm = new FormGroup({
    'emailFormControl' : new FormControl('', [Validators.required, Validators.email]),
    'phoneNumberFormControl' : new FormControl('', [Validators.required, Validators.pattern("[0-9]{9}")]),
    'nameFormControl' : new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
    'surnameFormControl' : new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
    'passwordAgainFormControl' : new FormControl('', [Validators.required, matchPasswordsValidator, Validators.minLength(8)]),
    'passwordFormControl' : new FormControl('',[Validators.required, Validators.minLength(9)]),
    'cityFormControl' : new FormControl('',[Validators.required, Validators.pattern('[a-zA-Z ]*')],),
  });
  
  matcher = new MyErrorStateMatcher();
  cities: string[] = ['Belgrade', 'Novi Sad', 'Kraljevo', 'Sabac'];
  registrationSubscription: Subscription;
  
  showDriverForm: boolean = this.authService.userIsAdmin();
  hidePassword: boolean =true;
  hideConfirmPassword: boolean =true;

  petFriendly: boolean = false;
  babySeat: boolean = false;
  vehicleType: string;

  ngOnInit(): void {}

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private toast: NgToastService,
    private router: Router
    ) {
    this.filteredCities = this.registrationForm.get('cityFormControl').valueChanges.pipe(
      startWith(''),
      map(city=> (city ? this._filterCities(city) : this.cities.slice())),
    );
  } 


  _filterCities(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.cities.filter(city => city.toLowerCase().includes(filterValue));
  }

  register(){
    if (isFormValid(this.registrationForm)){
      if (this.showDriverForm) {
        this.registrationSubscription = this.userService.registerDriver(
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
        ).subscribe(
          res => {
            this.toast.success({detail:"Registration successfull", summary:"Please go to " + res.email + " to verify account!", 
            duration:4000, position:'bl'})
            this.router.navigate(['/home-page'])
        }, 
          error => this.toast.error({detail:"Registration failed", summary:error.error, 
                  duration:4000, position:'bl'})
        );
      } else {
        this.registrationSubscription = this.userService.registerRegularUser(new RegistrationRequest(
          this.registrationForm.get('emailFormControl').value,
          this.registrationForm.get('passwordFormControl').value,
          this.registrationForm.get('passwordAgainFormControl').value,
          this.registrationForm.get('nameFormControl').value,
          this.registrationForm.get('surnameFormControl').value,
          this.registrationForm.get('phoneNumberFormControl').value,
          this.registrationForm.get('cityFormControl').value,
        )).subscribe(
          res => {
            this.toast.success({detail:"Registration successfull", summary:"Please go to " + res.email + " to verify account!", 
            duration:4000, position:'bl'})
            this.router.navigate(['/login'])
        }, 
          error => this.toast.error({detail:"Registration failed", summary:error.error, 
                  duration:4000, position:'bl'})
        );
      }
    }
  }

  ngOnDestroy(): void {
    if (this.registrationSubscription){
      this.registrationSubscription.unsubscribe();
    }
  }

}


export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

