import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroupDirective, NgForm, Validators} from '@angular/forms';
import {ErrorStateMatcher} from '@angular/material/core';
import { Subscription } from 'rxjs';
import { RegistrationService } from 'src/app/service/registration.service';
import { RegistrationRequest } from 'src/app/model/registration-request';


@Component({
  selector: 'app-registration',
  styleUrls: ['./registration.component.css'],
  templateUrl:'./registration.component.html'
})
export class RegistrationComponent implements OnInit{
  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  phoneNumberFormControl = new FormControl('', [Validators.required]);
  nameFormControl = new FormControl('', [Validators.required]);
  lastNameFormControl = new FormControl('', [Validators.required]);
  passwordAgainFormControl = new FormControl('', [Validators.required]);
  addressFormControl = new FormControl('', [Validators.required]);
  passwordFormControl = new FormControl('',[Validators.required],);
  addressNumberFormControl = new FormControl('',[Validators.required],);
  cityFormControl = new FormControl('',[Validators.required],);
  zipCodeFormControl = new FormControl('',[Validators.required],);
  matcher = new MyErrorStateMatcher();
  
  ngOnInit(): void {
  }

  registrationSubscription: Subscription;
  constructor(private registrationService: RegistrationService) {
    
  }
  register(){

    this.registrationSubscription = this.registrationService.register(new RegistrationRequest(
      this.emailFormControl.value,
      this.passwordFormControl.value,
      this.passwordAgainFormControl.value,
      this.nameFormControl.value,
      this.lastNameFormControl.value,
      this.phoneNumberFormControl.value,
      this.zipCodeFormControl.value,
      this.cityFormControl.value,
      this.addressNumberFormControl.value,
      this.addressFormControl.value
    )).subscribe();
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

