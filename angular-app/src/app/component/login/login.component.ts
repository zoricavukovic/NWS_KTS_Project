import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';

import {FormControl, FormGroup, Validators} from '@angular/forms';
import { Subscription } from 'rxjs';
import { LoginRequest } from 'src/app/model/login-request';
import { AuthService } from 'src/app/service/auth.service';
import { FacebookLoginProvider, SocialAuthService } from "@abacritt/angularx-social-login";
import { SocialUser } from "@abacritt/angularx-social-login";
import { isFormValid } from 'src/app/util/validation-function';
import { User } from 'src/app/model/user';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})

export class LoginComponent implements OnInit, OnDestroy {
  
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [Validators.required, Validators.minLength(8)])
  });

  hide: boolean =true;
  subscriptionLoginWithGmail: Subscription;
  authSubscription: Subscription;
  user: SocialUser;
  constructor(private authService: SocialAuthService,
              private social: AuthService,
              private router: Router
  ) { }

  ngOnInit(): void {
    this.signInWithGoogle();
  }

  signInWithGoogle(): void {
    this.authService.authState.subscribe((user) => {
        this.social.loginWithGoogle(user.idToken).subscribe(
          (res: User) => {
            this.user = user;
            this.router.navigate(['/home-page']);
          }
        ), (error) => {console.log(error)};
      }
    ), (error) => {console.log("greak")};;
  }

  signInWithFB(): void {
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID).then(
      
      data => {
        this.social.loginWithFacebook(data.authToken).subscribe(
          res => {
            this.router.navigate(['/home-page']);
          }
        );
      }
    );
  }

  signOut(): void {
    this.authService.signOut();
    this.router.navigate(['/home-page']);
  }

  getErrorMessage() {
    if (this.loginForm.get('email').hasError('required')) {
      return 'Email is required';
    }

    return this.loginForm.get('email').hasError('email') ? 'Not a valid email' : '';
  }

  logIn(){
    if (isFormValid(this.loginForm)){
      this.authSubscription = this.social.login(new LoginRequest(
        this.loginForm.get('email').value,
        this.loginForm.get('password').value
      )).subscribe(

        res => {
          this.router.navigate(['/home-page'])
          console.log(res);
        }
      );
    }
  }

  ngOnDestroy(): void {
    if (this.authSubscription){
      this.authSubscription.unsubscribe();
    }
    if (this.subscriptionLoginWithGmail){
      this.subscriptionLoginWithGmail.unsubscribe();
    }
  }
}
