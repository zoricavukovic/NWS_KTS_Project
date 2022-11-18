import { Component, OnDestroy, OnInit } from '@angular/core';

import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { LoginRequest } from 'src/app/model/request/user/login-request';
import { AuthService } from 'src/app/service/auth.service';
import {
  FacebookLoginProvider,
  SocialAuthService,
} from '@abacritt/angularx-social-login';
import { isFormValid } from 'src/app/util/validation-function';
import { Router } from '@angular/router';
import { LoginResponse } from 'src/app/model/response/user/login';
import { ToastrService } from 'ngx-toastr';
import { ChatService } from 'src/app/service/chat.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit, OnDestroy {
  loginForm = new FormGroup({
    email: new FormControl('', [Validators.required, Validators.email]),
    password: new FormControl('', [
      Validators.required,
      Validators.minLength(8),
    ]),
  });

  hide = true;
  subscriptionLoginWithGmail: Subscription;
  authSubscription: Subscription;

  constructor(
    private authService: SocialAuthService,
    private social: AuthService,
    private router: Router,
    private toast: ToastrService,
    private chatService: ChatService
  ) {}

  ngOnInit(): void {
    this.signInWithGoogle();
  }

  signInWithGoogle(): void {
    const router = this.router;
    const toast = this.toast;
    const chatService = this.chatService;
    this.authService.authState.subscribe(user => {
      const authService = this.social;
      authService.loginWithGoogle(user.idToken).subscribe({
        next(loggedUser: LoginResponse): void {
          authService.setLocalStorage(loggedUser);
          chatService.connect();
          authService.currentUserSubject$.next(loggedUser.userDTO);
          router.navigate(['/home-page']);
        },
        error(): void {
          toast.error('Email or password is not correct!', 'Login failed');
        },
      });
    });
  }

  signInWithFB(): void {
    const router = this.router;
    const toast = this.toast;
    const chatService = this.chatService;
    this.authService.signIn(FacebookLoginProvider.PROVIDER_ID).then(data => {
      const authService = this.social;
      this.social.loginWithFacebook(data.authToken).subscribe({
        next(loggedUser: LoginResponse): void {
          authService.setLocalStorage(loggedUser);
          chatService.connect();
          authService.currentUserSubject$.next(loggedUser.userDTO);
          router.navigate(['/home-page']);
        },
        error(): void {
          toast.error('Email or password is not correct!', 'Login failed');
        },
      });
    });
  }

  getErrorMessage() {
    if (this.loginForm.get('email').hasError('required')) {
      return 'Email is required';
    }

    return this.loginForm.get('email').hasError('email')
      ? 'Not a valid email'
      : '';
  }

  logIn() {
    if (isFormValid(this.loginForm)) {
      const router = this.router;
      const toast = this.toast;
      const authService = this.social;
      const chatService = this.chatService;
      this.authSubscription = this.social
        .login(
          new LoginRequest(
            this.loginForm.get('email').value,
            this.loginForm.get('password').value
          )
        )
        .subscribe({
          next(loggedUser: LoginResponse): void {
            authService.setLocalStorage(loggedUser);
            chatService.connect();
            authService.currentUserSubject$.next(loggedUser.userDTO);
            router.navigate(['/home-page']);
          },
          error(): void {
            toast.error('Email or password is not correct!', 'Login failed');
          },
        });
    }
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
    if (this.subscriptionLoginWithGmail) {
      this.subscriptionLoginWithGmail.unsubscribe();
    }
  }
}
