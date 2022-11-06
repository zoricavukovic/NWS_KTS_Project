import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/service/auth.service';
import { UserService } from 'src/app/service/user.service';
import { matchPasswordsValidator } from '../registration/confirm-password.validator';
import { MyErrorStateMatcher } from '../registration/registration.component';
import { isFormValid } from 'src/app/util/validation-function';
import { UserPasswordUpdateRequest } from 'src/app/model/user-password-update-request';


@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent implements OnInit {

  changePassForm = new FormGroup({
    'oldPasswordFormControl' : new FormControl('', [Validators.required]),
    'passwordAgainFormControl' : new FormControl('', [Validators.required, matchPasswordsValidator]),
    'passwordFormControl' : new FormControl('',[Validators.required]),
  });

  matcher = new MyErrorStateMatcher();

  hidePassword: boolean =true;
  hideConfirmPassword: boolean =true;
  hideOldPassword: boolean = true;

  currentUser: User = this.authService.getCurrentUser();

  constructor(
    private authService: AuthService,
    private userService: UserService
  ) {}

  changePassword() {
    if (isFormValid(this.changePassForm)){
      this.userService.updatePassword(
      new UserPasswordUpdateRequest(
        this.currentUser.email,
        this.changePassForm.get('oldPasswordFormControl').value,
        this.changePassForm.get('passwordFormControl').value,
        this.changePassForm.get('passwordAgainFormControl').value
      )
    );
    }
  }

  checkValidForm() {
    return !isFormValid(this.changePassForm);
  }

  ngOnInit(): void {
  }

}
