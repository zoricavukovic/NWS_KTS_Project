import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { getMatFormFieldMissingControlError } from '@angular/material/form-field';
import { ActivatedRoute } from '@angular/router';
import { PasswordUpdateRequest } from 'src/app/model/password-update-request';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';
import { isFormValid } from 'src/app/util/validation-function';
import { matchPasswordsValidator } from '../registration/confirm-password.validator';
import { MyErrorStateMatcher } from '../registration/registration.component';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent implements OnInit {

  email: string;
  hidePassword: boolean =true;
  hideConfirmPassword: boolean =true;
  matcher = new MyErrorStateMatcher();
  passwordForm = new FormGroup({
    'passwordAgainFormControl' : new FormControl('', [Validators.required, matchPasswordsValidator]),
    'passwordFormControl' : new FormControl('',[Validators.required])
  });

  constructor(private route: ActivatedRoute, private userService: UserService) { }

  ngOnInit(): void {
    this.email = this.route.snapshot.paramMap.get('email');
  }

  resetPasswordEmail() {
    if (isFormValid(this.passwordForm)){
      this.userService.resetPassword(new PasswordUpdateRequest(
        this.email,
        this.passwordForm.get('passwordFormControl').value,
        this.passwordForm.get('passwordAgainFormControl').value 
      ));
    }
  }
}
