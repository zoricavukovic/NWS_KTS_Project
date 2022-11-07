import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { getMatFormFieldMissingControlError } from '@angular/material/form-field';
import { ActivatedRoute } from '@angular/router';
import { PasswordUpdateRequest } from 'src/app/model/password-update-request';
import { User } from 'src/app/model/user';
import { UserPasswordUpdateRequest } from 'src/app/model/user-password-update-request';
import { AuthService } from 'src/app/service/auth.service';
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
  @Input() public user: User;

  email: string;
  hidePassword: boolean =true;
  hideConfirmPassword: boolean =true;
  hideOldPassword: boolean = true;
  matcher = new MyErrorStateMatcher();
  oldPasswordForm: FormControl = new FormControl('', [Validators.required, Validators.minLength(8)]);

  passwordForm = new FormGroup({
    'passwordAgainFormControl' : new FormControl('', [Validators.required, matchPasswordsValidator, Validators.minLength(8)]),
    'passwordFormControl' : new FormControl('',[Validators.required, Validators.minLength(8)])
  });

  constructor(
    private route: ActivatedRoute, 
    private userService: UserService
    ) { }

  ngOnInit(): void {
    this.email = this.route.snapshot.paramMap.get('email');
    if ((this.email === null || this.email === undefined) && this.userIsLogged()) {
      this.email = this.user.email;
    }
  }

  userIsLogged(): boolean {
    return (this.user !== null && this.user !== undefined);
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

  changePassword() {
    if (isFormValid(this.passwordForm) && !this.oldPasswordForm.invalid){
      this.userService.updatePassword(new UserPasswordUpdateRequest(
        this.user.email,
        this.oldPasswordForm.value,
        this.passwordForm.get('passwordFormControl').value,
        this.passwordForm.get('passwordAgainFormControl').value
      )
    );
    }
  }


}
