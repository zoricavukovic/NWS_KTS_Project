import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import {ActivatedRoute, Router} from '@angular/router';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import {User} from "../../../shared/models/user/user";
import {PasswordUpdateRequest} from "../../../shared/models/user/user-profile-update";
import {ConfigService} from "../../../shared/services/config-service/config.service";
import {UserService} from "../../../shared/services/user-service/user.service";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {MyErrorStateMatcher} from "../registration/registration.component";
import {matchPasswordsValidator} from "../registration/confirm-password.validator";

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
})
export class ResetPasswordComponent implements OnInit, OnDestroy {
  email: string;
  hidePassword = true;
  hideConfirmPassword = true;
  hideOldPassword = true;
  matcher = new MyErrorStateMatcher();

  resetSubscription: Subscription;
  changePassSubscription: Subscription;
  authSubscription: Subscription;

  user: User;
  userIsLogged: boolean;

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
    this.userIsLogged = false;
    this.user = null;
  }

  ngOnInit(): void {
    this.email = this.route.snapshot.paramMap.get('email');
    this.authSubscription = this.authService.getSubjectCurrentUser().subscribe(
      res => {
        if (res) {
          this.userIsLogged = true;
          this.user = this.user;
        }
      });
  }

  resetPasswordEmail() {
    if (!this.passwordForm.invalid) {
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
            this.router.navigate(['/serb-uber/auth/login']);
          },
          error => this.toast.error(error.error, 'Reset password failed')
        );
    }
  }

  changePassword() {
    if (!this.passwordForm.invalid && !this.oldPasswordForm.invalid) {
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
            this.router.navigate(['/serb-uber/auth/login']);
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
