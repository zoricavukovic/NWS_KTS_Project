import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, FormGroupDirective, NgForm, Validators, ValidatorFn, ValidationErrors, AbstractControl } from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import { Observable, Subscription } from 'rxjs';
import {map, startWith} from 'rxjs/operators';
import { RegistrationService } from 'src/app/service/registration.service';
import { RegistrationRequest } from 'src/app/model/registration-request';
import { matchPasswordsValidator } from './confirm-password.validator';
import { VehicleTypeInfo } from 'src/app/model/vehicle-type-info-response';
import { AuthService } from 'src/app/service/auth.service';
import { DriverRegistrationRequest } from 'src/app/model/driver-registration-request';
import { VehicleRequest } from 'src/app/model/vehicle-request';

@Component({
  selector: 'app-registration',
  styleUrls: ['./registration.component.css'],
  templateUrl:'./registration.component.html'
})
export class RegistrationComponent implements OnInit{

  filteredCities: Observable<string[]>;
  registrationForm = new FormGroup({
    'emailFormControl' : new FormControl('', [Validators.required, Validators.email]),
    'phoneNumberFormControl' : new FormControl('', [Validators.required, Validators.pattern("[0-9]*")]),
    'nameFormControl' : new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
    'surnameFormControl' : new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
    'passwordAgainFormControl' : new FormControl('', [Validators.required, matchPasswordsValidator]),
    'passwordFormControl' : new FormControl('',[Validators.required]),
    'cityFormControl' : new FormControl('',[Validators.required],),
  });
  
  matcher = new MyErrorStateMatcher();
  cities: string[] = ['Belgrade', 'Novi Sad', 'Kraljevo', 'Sabac'];
  registrationSubscription: Subscription;
  petFriendly: boolean = false;
  babySeat: boolean = false;
  selectedVehicleType: string;
  vehicleTypes: Observable<VehicleTypeInfo[]>;
  //showDriverForm: boolean = this.authService.userIsAdmin();
  showDriverForm: boolean = true;

  ngOnInit(): void {
    this.vehicleTypes = this.registrationService.getVehicleTypeInfos();
    console.log(this.vehicleTypes)
  }

  constructor(
    private registrationService: RegistrationService,
    private authService: AuthService
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

  changeSelectedVehicleType(value) {
    console.log(value);
    this.selectedVehicleType = value;
  }

  register(){
    if (this.showDriverForm) {
      this.registrationSubscription = this.registrationService.registerDriver(
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
            this.selectedVehicleType
          )
        )
      ).subscribe();
    } else {
      this.registrationSubscription = this.registrationService.registerRegularUser(new RegistrationRequest(
        this.registrationForm.get('emailFormControl').value,
        this.registrationForm.get('passwordFormControl').value,
        this.registrationForm.get('passwordAgainFormControl').value,
        this.registrationForm.get('nameFormControl').value,
        this.registrationForm.get('surnameFormControl').value,
        this.registrationForm.get('phoneNumberFormControl').value,
        this.registrationForm.get('cityFormControl').value,
      )).subscribe();
    }
  }

  ngOnDestroy(): void {
    this.registrationSubscription.unsubscribe();
  }

}


export class MyErrorStateMatcher implements ErrorStateMatcher {
  isErrorState(control: FormControl | null, form: FormGroupDirective | NgForm | null): boolean {
    const isSubmitted = form && form.submitted;
    return !!(control && control.invalid && (control.dirty || control.touched || isSubmitted));
  }
}

