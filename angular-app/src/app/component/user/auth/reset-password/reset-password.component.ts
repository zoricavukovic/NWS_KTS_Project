import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { PasswordUpdateRequest } from 'src/app/model/request/user/user-profile-update';
import { User } from 'src/app/model/response/user/user';
import { UserPasswordUpdateRequest } from 'src/app/model/request/user/user-profile-update';
import { AuthService } from 'src/app/service/auth.service';
import { UserService } from 'src/app/service/user.service';
import { isFormValid } from 'src/app/util/validation-function';
import { matchPasswordsValidator } from '../registration/confirm-password.validator';
import { MyErrorStateMatcher } from '../registration/registration.component';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
})
export class ResetPasswordComponent implements OnInit, OnDestroy {
  @Input() public user: User;

  email: string;
  hidePassword = true;
  hideConfirmPassword = true;
  hideOldPassword = true;
  matcher = new MyErrorStateMatcher();

  resetSubscription: Subscription;
  changePassSubscription: Subscription;

  oldPasswordForm: FormControl = new FormControl('', [
    Validators.required,
    Validators.minLength(8),
  ]);

  passwordForm = new FormGroup({
    passwordAgainFormControl: new FormControl('', [
      Validators.required,
      matchPasswordsValidator,
      Validators.minLength(8),
    ]),
    passwordFormControl: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
    ]),
  });

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private authService: AuthService,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.email = this.route.snapshot.paramMap.get('email');
    if (!this.email && this.userIsLogged()) {
      this.email = this.user.email;
    }
  }

  userIsLogged(): boolean {
    return this.user !== null && this.user !== undefined;
  }

  resetPasswordEmail() {
    if (isFormValid(this.passwordForm)) {
      this.resetSubscription = this.userService
        .resetPassword(
          new PasswordUpdateRequest(
            this.email,
            this.passwordForm.get('passwordFormControl').value,
            this.passwordForm.get('passwordAgainFormControl').value
          )
        )
        .subscribe(
          response => {
            console.log(response);
            this.authService.logOut();
          },
          error => this.toast.error(error.error, 'Reset password failed')
        );
    }
  }

  changePassword() {
    if (isFormValid(this.passwordForm) && !this.oldPasswordForm.invalid) {
      this.changePassSubscription = this.userService
        .updatePassword(
          new UserPasswordUpdateRequest(
            this.user.email,
            this.oldPasswordForm.value,
            this.passwordForm.get('passwordFormControl').value,
            this.passwordForm.get('passwordAgainFormControl').value
          )
        )
        .subscribe(
          response => {
            this.authService.logOut();
            console.log(response);
          },
          error => this.toast.error(error.error, 'Reset password failed')
        );
    }
  }

  ngOnDestroy(): void {
    if (this.resetSubscription) {
      this.resetSubscription.unsubscribe();
    }

    if (this.changePassSubscription) {
      this.changePassSubscription.unsubscribe();
    }
  }
}
