import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, Validators, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UserService } from 'src/app/service/user.service';
import { isFormValid } from 'src/app/util/validation-function';
import { NgToastService } from 'ng-angular-popup';

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
              private toast: NgToastService
    ) { }

  ngOnInit(): void {}

  sendResetPasswordEmail(){
    if (isFormValid(this.enterEmailForm)){
      this.sendResetPassEmailSubscription = this.userService.sendResetPasswordEmail(this.enterEmailForm.get('email').value)
      .subscribe(
        res => this.toast.success({detail: "Reset link sent", summary: "Reset link is sent to email succesffully."}), 
        error => this.toast.error({detail:"Reset link not sent", summary:"Email cannot be sent! Try again.", 
        duration:4000, position:'bl'})
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
