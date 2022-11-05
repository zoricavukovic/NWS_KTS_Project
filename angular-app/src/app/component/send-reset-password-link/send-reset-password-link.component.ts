import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormGroup } from '@angular/forms';
import { UserService } from 'src/app/service/user.service';
import { isFormValid } from 'src/app/util/validation-function';

@Component({
  selector: 'app-send-reset-password-link',
  templateUrl: './send-reset-password-link.component.html',
  styleUrls: ['./send-reset-password-link.component.css']
})
export class SendResetPasswordEmailComponent implements OnInit {

  enterEmailForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email])
  });
  constructor(private userService: UserService) { }
  

  ngOnInit(): void {}

  sendResetPasswordEmail(){
    if (isFormValid(this.enterEmailForm)){
      this.userService.sendResetPasswordEmail(this.enterEmailForm.get('email').value);
    } 
  }

  getErrorMessage() {
    if (this.enterEmailForm.get('email').hasError('required')) {

      return 'Email is required';
    }

    return this.enterEmailForm.get('email').hasError('email') ? 'Not a valid email' : '';
  }
}
