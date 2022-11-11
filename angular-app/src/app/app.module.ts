import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';
import { LoginComponent } from './component/user/auth/login/login.component';
import { SocialLoginModule, SocialAuthServiceConfig } from '@abacritt/angularx-social-login';
import { GoogleLoginProvider, FacebookLoginProvider } from '@abacritt/angularx-social-login';
import { environment } from 'src/environments/environment';
import { RegistrationComponent } from './component/user/auth/registration/registration.component';
import { VerifyComponent } from './component/user/auth/verify/verify.component';
import { DriverVehicleComponent } from './component/vehicle/create-vehicle/driver-vehicle.component';
import {CarouselModule} from 'primeng/carousel';
import { ShowDrivingsComponent } from './component/driving/show-drivings/show-drivings.component';
import { DrivingDetailsComponent } from './component/driving/driving-details/driving-details.component';
import { RatingDialogComponent } from './component/review/rating-dialog/rating-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { SendResetPasswordEmailComponent } from './component/user/auth/send-reset-password-link/send-reset-password-link.component';
import { ResetPasswordComponent } from './component/user/auth/reset-password/reset-password.component';
import { HomePageComponent } from './component/home-page/home-page.component';
import { TimelineModule } from 'primeng/timeline';
import { NavBarComponent} from './component/nav-bar/nav-bar.component';
import {RatingModule} from 'primeng/rating';
import { NgxStarsModule } from 'ngx-stars';
import { DrivingRowComponent } from './component/driving/driving-row/driving-row.component';
import { ProfilePageComponent } from './component/user/profile-page/profile-page.component';
import { EditProfileComponent } from './component/user/edit-profile/edit-profile.component';
import { ChangeProfilePicComponent } from './component/user/change-profile-pic/change-profile-pic.component';
import { ToastrModule } from 'ngx-toastr';
import { ButtonLiveChatComponent } from './component/chat/button-live-chat/button-live-chat.component';
import { PoupupLiveChatComponent } from './component/chat/poupup-live-chat/poupup-live-chat.component';
import { HistoryLiveChatComponent } from './component/chat/history-live-chat/history-live-chat.component';


@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    RegistrationComponent,
    VerifyComponent,
    DriverVehicleComponent,
    ShowDrivingsComponent,
    DrivingDetailsComponent,
    RatingDialogComponent,
    SendResetPasswordEmailComponent,
    ResetPasswordComponent,
    HomePageComponent,
    NavBarComponent,
    DrivingRowComponent,
    ProfilePageComponent,
    EditProfileComponent,
    ChangeProfilePicComponent,
    ButtonLiveChatComponent,
    PoupupLiveChatComponent,
    HistoryLiveChatComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    HttpClientModule,
    MaterialModule,
    FormsModule,
    ReactiveFormsModule,
    SocialLoginModule,
    CarouselModule,
    MatDialogModule,
    RatingModule,
    TimelineModule,
    NgxStarsModule,
    ToastrModule.forRoot({
      timeOut: 4000,
      positionClass: 'toast-bottom-left',
      preventDuplicates: true,
      closeButton: true
    }),
  ],
  entryComponents:[EditProfileComponent],
  providers: [
    {
      provide: 'SocialAuthServiceConfig',
      useValue: {
        autoLogin: false,
        providers: [
          {
            id: GoogleLoginProvider.PROVIDER_ID,
            provider: new GoogleLoginProvider(
              environment.clientId
            )
          },
          {
            id: FacebookLoginProvider.PROVIDER_ID,
            provider: new FacebookLoginProvider(environment.facebookAppId)
          }
        ],
        onError: (err) => {
          console.error(err);
        }
      } as SocialAuthServiceConfig,
    },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
