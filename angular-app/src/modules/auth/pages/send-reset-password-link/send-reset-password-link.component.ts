import { Component, OnDestroy } from '@angular/core';
import { FormControl, Validators, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import {UserService} from "../../../shared/services/user-service/user.service";

@Component({
  selector: 'app-send-reset-password-link',
  templateUrl: './send-reset-password-link.component.html',
  styleUrls: ['./send-reset-password-link.component.css'],
})
export class SendResetPasswordEmailComponent implements OnDestroy {
  enterEmailForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
  });

  sendResetPassEmailSubscription: Subscription;

  constructor(private userService: UserService, private toast: ToastrService) {}

  sendResetPasswordEmail() {
    if (!this.enterEmailForm.invalid) {
      this.sendResetPassEmailSubscription = this.userService
        .sendResetPasswordEmail(this.enterEmailForm.get('email').value)
        .subscribe(
          response => {
            console.log(response);
            this.toast.success(
              'Reset link is sent to email successfully.',
              'Reset link sent'
            );
            this.resetForm();
          },
          error => this.toast.error(error.error, 'Reset link not sent')
        );
    }
  }

  getErrorMessage() {
    if (this.enterEmailForm.get('email').hasError('required')) {
      return 'Email is required';
    }

    return this.enterEmailForm.get('email').hasError('email')
      ? 'Not a valid email'
      : '';
  }

  ngOnDestroy(): void {
    if (this.sendResetPassEmailSubscription) {
      this.sendResetPassEmailSubscription.unsubscribe();
    }
  }

  private resetForm() {
    this.enterEmailForm.reset();
    Object.keys(this.enterEmailForm.controls).forEach(key => {
      this.enterEmailForm.get(key).setErrors(null);
    });
  }
}
