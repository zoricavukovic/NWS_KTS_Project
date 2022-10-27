import { Component, OnInit } from '@angular/core';
import { FormControl, Validators, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { LoginRequest } from 'src/app/model/login-request';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(8)])
  });

  hide: boolean =true;

  authSubscription: Subscription;
  constructor(
    private authService: AuthService
  ) { }

  ngOnInit(): void {
  }

  getErrorMessage() {
    if (this.loginForm.get('email').hasError('required')) {
      return 'You must enter a value';
    }

    return this.loginForm.get('email').hasError('email') ? 'Not a valid email' : '';
  }

  logIn(){

    this.authSubscription = this.authService.login(new LoginRequest(
      this.loginForm.get('email').value,
      this.loginForm.get('password').value
    )).subscribe();
  }

  ngOnDestroy(): void {
    this.authSubscription.unsubscribe();
  }
}
