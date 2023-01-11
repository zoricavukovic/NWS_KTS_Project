import { Component, OnDestroy, OnInit } from '@angular/core';

import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import {
  FacebookLoginProvider,
  SocialAuthService,
} from '@abacritt/angularx-social-login';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import {LoginResponse} from "../../../shared/models/user/login-response";
import {LoginRequest} from "../../../shared/models/user/login-request";
import {AuthService} from "../../services/auth-service/auth.service";
import {WebSocketService} from "../../../shared/services/web-socket-service/web-socket.service";

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
    private chatService: WebSocketService
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
          router.navigate(['/serb-uber/user/map-page-view/-1']);
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
          router.navigate(['/serb-uber/user/map-page-view/-1']);
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
    if (!this.loginForm.invalid) {
      const router = this.router;
      const toast = this.toast;
      const authService = this.social;
      const chatService = this.chatService;
      const loginRequest: LoginRequest = {
        email: this.loginForm.get('email').value,
        password: this.loginForm.get('password').value,
      };
      this.authSubscription = this.social.login(loginRequest).subscribe({
        next(loggedUser: LoginResponse): void {
          authService.setLocalStorage(loggedUser);
          chatService.connect();
          router.navigate(['/serb-uber/user/map-page-view/-1']);
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
