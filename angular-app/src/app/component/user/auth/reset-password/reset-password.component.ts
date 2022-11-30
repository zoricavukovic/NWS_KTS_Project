import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { PasswordUpdateRequest } from 'src/app/model/user/user-profile-update';
import { User } from 'src/app/model/user/user';
import { AuthService } from 'src/app/service/auth.service';
import { UserService } from 'src/app/service/user.service';
import { isFormValid } from 'src/app/util/validation-function';
import { matchPasswordsValidator } from '../registration/confirm-password.validator';
import { MyErrorStateMatcher } from '../registration/registration.component';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import {ConfigService} from "../../../../service/config.service";

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
    public authService: AuthService,
    public configService: ConfigService,
    private route: ActivatedRoute,
    private userService: UserService,
    private toast: ToastrService,
    private router: Router
  ) {
    this.user = null;
  }

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
      const passwordUpdateRequest: PasswordUpdateRequest = {
        email: this.email,
        newPassword: this.passwordForm.get('passwordFormControl').value,
        confirmPassword: this.passwordForm.get('passwordAgainFormControl')
          .value,
      };
      this.resetSubscription = this.userService
        .resetPassword(passwordUpdateRequest)
        .subscribe(
          response => {
            this.toast.success("Password is changed successfully.", 'Changed password')
            this.router.navigate(['/login']);
          },
          error => this.toast.error(error.error, 'Reset password failed')
        );
    }
  }

  changePassword() {
    if (isFormValid(this.passwordForm) && !this.oldPasswordForm.invalid) {
      const passwordUpdateRequest: PasswordUpdateRequest = {
        email: this.email,
        newPassword: this.passwordForm.get('passwordFormControl').value,
        confirmPassword: this.passwordForm.get('passwordAgainFormControl')
          .value,
        currentPassword: this.oldPasswordForm.value,
      };
      this.changePassSubscription = this.userService
        .updatePassword(passwordUpdateRequest)
        .subscribe(
          response => {
            this.toast.success("Password is changed successfully.", 'Updated password')
            this.authService.logOut();
            this.router.navigate(['/login']);
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
