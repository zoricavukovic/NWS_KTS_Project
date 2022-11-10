import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, Validators, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UserService } from 'src/app/service/user.service';
import { isFormValid } from 'src/app/util/validation-function';
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-send-reset-password-link',
  templateUrl: './send-reset-password-link.component.html',
  styleUrls: ['./send-reset-password-link.component.css']
})
export class SendResetPasswordEmailComponent implements OnInit, OnDestroy {

  enterEmailForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email])
  });

  sendResetPassEmailSubscription: Subscription;

  constructor(private userService: UserService,
              private toast: ToastrService
    ) { }

  ngOnInit(): void {}

  sendResetPasswordEmail(){
    if (isFormValid(this.enterEmailForm)){
      this.sendResetPassEmailSubscription = this.userService.sendResetPasswordEmail(this.enterEmailForm.get('email').value)
      .subscribe(
        res => this.toast.success("Reset link is sent to email successfully.", "Reset link sent"),
        error => this.toast.error(error.error, "Reset link not sent")
      )
    }
  }

  getErrorMessage() {
    if (this.enterEmailForm.get('email').hasError('required')) {

      return 'Email is required';
    }

    return this.enterEmailForm.get('email').hasError('email') ? 'Not a valid email' : '';
  }

  ngOnDestroy(): void {
    if (this.sendResetPassEmailSubscription) {
      this.sendResetPassEmailSubscription.unsubscribe();
    }
  }

}
