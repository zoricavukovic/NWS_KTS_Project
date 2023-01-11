import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {LoginComponent} from "./pages/login/login.component";
import {
  FacebookLoginProvider,
  GoogleLoginProvider,
  SocialAuthServiceConfig,
  SocialLoginModule
} from "@abacritt/angularx-social-login";
import {environment} from "../../environments/environment";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {AuthRoutes} from "./auth.routes";
import {MaterialModule} from "../material/material.module";
import {SharedModule} from "../shared/shared.module";
import {SendResetPasswordEmailComponent} from "./pages/send-reset-password-link/send-reset-password-link.component";
import {SuccessfullVerificationComponent} from "./pages/successfull-verification/successfull-verification.component";
import {VerifyComponent} from "./pages/verify/verify.component";


@NgModule({
  declarations: [
    LoginComponent,
    SendResetPasswordEmailComponent,
    SuccessfullVerificationComponent,
    VerifyComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MaterialModule,
    RouterModule.forChild(AuthRoutes),
    SharedModule,
    SocialLoginModule
  ],
  providers: [
    {
      provide: SocialLoginModule,
      useValue: new SocialLoginModule(null),
    },
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(environment.clientId),
          },
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider(environment.facebookAppId),
          },
        ],
        onError: err => {
          console.error(err);
        },
      } as SocialAuthServiceConfig,
    }
  ]
})
export class AuthModule { }
