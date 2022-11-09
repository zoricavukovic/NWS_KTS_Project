import { Component, EventEmitter, OnDestroy, OnInit, Output } from '@angular/core';

import {FormControl, FormGroup, Validators} from '@angular/forms';
import { Subscription } from 'rxjs';
import { LoginRequest } from 'src/app/model/request/user/login-request';
import { AuthService } from 'src/app/service/auth.service';
import { FacebookLoginProvider, SocialAuthService } from "@abacritt/angularx-social-login";
import { isFormValid } from 'src/app/util/validation-function';
import { Router } from '@angular/router';
import { NgToastService } from 'ng-angular-popup';
import { LoginResponse } from 'src/app/model/response/user/login';

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

  constructor(private authService: SocialAuthService,
              private social: AuthService,
              private router: Router,
              private toast: NgToastService
  ) { }

  ngOnInit(): void {
    this.signInWithGoogle();
  }

  signInWithGoogle(): void {
    this.authService.authState.subscribe(
      (user) => {
        this.social.loginWithGoogle(user.idToken).subscribe(
          res => {
            const loggedUser = res as LoginResponse;
            this.social.setLocalStorage(loggedUser); 
            this.social.currentUser$.next(loggedUser.userDTO);
            this.router.navigate(['/home-page']);
        }, 
        error => this.toast.error({detail:"Login failed", summary:"Email or password is not correct!", 
              duration:4000, position:'bl'})
    )}, 
      error => this.toast.error({detail:"Login failed", summary:"Email or password is not correct!", 
              duration:4000, position:'bl'})
    )
  }

  signInWithFB(): void {
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID).then(
      
      data => {
        this.social.loginWithFacebook(data.authToken).subscribe(
          res => {
            const loggedUser = res as LoginResponse;
            this.social.setLocalStorage(loggedUser); 
            this.social.currentUser$.next(loggedUser.userDTO);
            this.router.navigate(['/home-page']);
          }, 
            error => this.toast.error({detail:"Login failed", summary:"Email or password is not correct!", 
              duration:4000, position:'bl'})
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
          const loggedUser = res as LoginResponse;
          this.social.setLocalStorage(loggedUser); 
          this.social.currentUser$.next(loggedUser.userDTO);
          this.router.navigate(['/home-page'])
        }, 
        error => this.toast.error({detail:"Login failed", summary:"Email or password is not correct!", 
        duration:4000, position:'bl'})
      )}
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
