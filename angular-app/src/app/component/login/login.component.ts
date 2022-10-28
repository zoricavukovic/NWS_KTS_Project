import { Component, OnInit } from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { LoginRequest } from 'src/app/model/login-request';
import { AuthService } from 'src/app/service/auth.service';
import { FacebookLoginProvider, GoogleLoginProvider, SocialAuthService } from "@abacritt/angularx-social-login";
import { SocialUser } from "@abacritt/angularx-social-login";
import { User } from 'src/app/model/user';

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
  subscriptionLoginWithGmail: Subscription;
  authSubscription: Subscription;

  user: SocialUser;
  isLogin: boolean;
  constructor(private authService: SocialAuthService,
              private social: AuthService) { }

  ngOnInit(): void {
    this.authService.authState.subscribe(
      data => {
        this.isLogin = (data != null);
      }
    );
  }

  signInWithGoogle(): void {
    this.authService.signIn(GoogleLoginProvider.PROVIDER_ID).then(
      data => {
        this.social.loginWithGoogle(data.idToken).subscribe(
          res => {
            console.log(res);
          }
        );
      }
    );
  }

  signInWithFB(): void {
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID).then(
      data => {
        this.social.loginWithFacebook(data.authToken).subscribe(
          res => {
            console.log(res);
          }
        );
      }
    );
  }
  signOut(): void {
    this.authService.signOut();
  }

  getErrorMessage() {
    if (this.loginForm.get('email').hasError('required')) {
      return 'You must enter a value';
    }

    return this.loginForm.get('email').hasError('email') ? 'Not a valid email' : '';
  }

  logIn(){

    this.authSubscription = this.social.login(new LoginRequest(
      this.loginForm.get('email').value,
      this.loginForm.get('password').value
    )).subscribe();
  }

  ngOnDestroy(): void {
    this.authSubscription.unsubscribe();
    this.subscriptionLoginWithGmail.unsubscribe();
  }
}
