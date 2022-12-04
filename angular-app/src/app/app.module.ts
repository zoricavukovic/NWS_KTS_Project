import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppComponent } from './app.component';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MaterialModule } from './material/material.module';
import { LoginComponent } from './component/user/auth/login/login.component';
import { GooglePlaceModule } from "ngx-google-places-autocomplete";
import {
  SocialLoginModule,
  SocialAuthServiceConfig,
} from '@abacritt/angularx-social-login';
import {
  GoogleLoginProvider,
  FacebookLoginProvider,
} from '@abacritt/angularx-social-login';
import { environment } from 'src/environments/environment';
import { RegistrationComponent } from './component/user/auth/registration/registration.component';
import { VerifyComponent } from './component/user/auth/verify/verify.component';
import { DriverVehicleComponent } from './component/vehicle/create-vehicle/driver-vehicle.component';
import { CarouselModule } from 'primeng/carousel';
import { ButtonModule } from 'primeng/button';
import { ShowDrivingsComponent } from './component/driving/show-drivings/show-drivings.component';
import { DrivingDetailsComponent } from './component/driving/driving-details/driving-details.component';
import { RatingDialogComponent } from './component/review/rating-dialog/rating-dialog.component';
import { MatDialogModule } from '@angular/material/dialog';
import { SendResetPasswordEmailComponent } from './component/user/auth/send-reset-password-link/send-reset-password-link.component';
import { ResetPasswordComponent } from './component/user/auth/reset-password/reset-password.component';
import { HomePageComponent } from './component/home/home-page/home-page.component';
import { TimelineModule } from 'primeng/timeline';
import { NavBarComponent } from './component/nav-bar/nav-bar.component';
import { RatingModule } from 'primeng/rating';
import { NgxStarsModule } from 'ngx-stars';
import { MglTimelineModule } from 'angular-mgl-timeline';
import { DrivingRowComponent } from './component/driving/driving-row/driving-row.component';
import { ProfilePageComponent } from './component/user/profile-page/profile-page.component';
import { EditProfileComponent } from './component/user/edit-profile/edit-profile.component';
import { ChangeProfilePicComponent } from './component/user/change-profile-pic/change-profile-pic.component';
import { NgToastModule } from 'ng-angular-popup';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatRadioModule } from '@angular/material/radio';
import { MatPaginatorModule } from '@angular/material/paginator';
import { MatChipsModule } from '@angular/material/chips';
import { ToastrModule } from 'ngx-toastr';
import { ButtonLiveChatComponent } from './component/chat/button-live-chat/button-live-chat.component';
import { PoupupLiveChatComponent } from './component/chat/poupup-live-chat/poupup-live-chat.component';
import { HistoryLiveChatComponent } from './component/chat/history-live-chat/history-live-chat.component';
import { ChatRoomsListComponent } from './component/chat/chat-rooms-list/chat-rooms-list.component';
import { MessagesBoxComponent } from './component/chat/messages-box/messages-box.component';
import { RouteRowComponent } from './component/home/route-row/route-row.component';
import { ShowDriversComponent } from './component/admin/show-drivers/show-drivers.component';
import { DriverRowComponent } from './component/admin/driver-row/driver-row.component';
import { ShowUsersComponent } from './component/admin/show-users/show-users.component';
import { UserRowComponent } from './component/admin/user-row/user-row.component';
import { VehicleDetailsComponent } from './component/vehicle/vehicle-details/vehicle-details.component';
import { MapComponent } from './component/map/map.component';
import { FavouriteRoutesComponent } from './component/favourite-route/favourite-routes/favourite-routes.component';
import { FavouriteRouteRowComponent } from './component/favourite-route/favourite-route-row/favourite-route-row.component';
import { UtilMessageComponent } from './component/util-message/util-message.component';
import { FilterVehicleViewComponent } from './component/home/filter-vehicle-view/filter-vehicle-view.component';
import { ScrollingModule } from '@angular/cdk/scrolling';
import { DriverHomePageContainerComponent } from './component/home/driver-home-page-container/driver-home-page-container.component';
import { AvatarModule } from 'primeng/avatar';
import { NgxPaginationModule } from 'ngx-pagination';
import { AvatarGroupModule } from 'primeng/avatargroup';
import { SuccessfullVerificationComponent } from './component/user/auth/successfull-verification/successfull-verification.component';
import { RejectDrivingComponent } from './component/driving/reject-driving/reject-driving.component';
import { BasicUserProfileComponent } from './component/user/basic-user-profile/basic-user-profile.component';
import { BasicUserDataComponent } from './component/user/basic-user-data/basic-user-data.component';
import { UserProfileReviewsComponent } from './component/user/user-profile-reviews/user-profile-reviews.component';
import { UserAverageRateComponent } from './component/review/user-average-rate/user-average-rate.component';
import { ReviewsHistoryComponent } from './component/review/reviews-history/reviews-history.component';
import { ReportsHistoryComponent } from './component/report/reports-history/reports-history.component';
import { ConfirmBlockingDialogComponent } from './component/user/confirm-blocking-dialog/confirm-blocking-dialog.component';
import {SplitButtonModule} from "primeng/splitbutton";

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
    ChatRoomsListComponent,
    MessagesBoxComponent,
    RouteRowComponent,
    ShowDriversComponent,
    DriverRowComponent,
    ShowUsersComponent,
    UserRowComponent,
    VehicleDetailsComponent,
    MapComponent,
    FavouriteRoutesComponent,
    FavouriteRouteRowComponent,
    UtilMessageComponent,
    FilterVehicleViewComponent,
    DriverHomePageContainerComponent,
    SuccessfullVerificationComponent,
    RejectDrivingComponent,
    BasicUserProfileComponent,
    BasicUserDataComponent,
    UserProfileReviewsComponent,
    UserAverageRateComponent,
    ReviewsHistoryComponent,
    ReportsHistoryComponent,
    ConfirmBlockingDialogComponent,
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
        ButtonModule,
        MatDialogModule,
        RatingModule,
        TimelineModule,
        NgxStarsModule,
        NgToastModule,
        MglTimelineModule,
        MatTooltipModule,
        MatRadioModule,
        MatPaginatorModule,
        MatChipsModule,
        ToastrModule.forRoot({
            positionClass: 'toast-bottom-left',
            preventDuplicates: true,
            closeButton: true,
        }),
        GooglePlaceModule,
        ScrollingModule,
        AvatarModule,
        AvatarGroupModule,
        NgxPaginationModule,
        SplitButtonModule,
    ],
  entryComponents: [EditProfileComponent],
  providers: [
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
    },
  ],
  bootstrap: [AppComponent],
})
export class AppModule {}
