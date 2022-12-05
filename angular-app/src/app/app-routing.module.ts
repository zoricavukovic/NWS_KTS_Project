import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { LoginComponent } from './component/user/auth/login/login.component';
import { RegistrationComponent } from './component/user/auth/registration/registration.component';
import { ShowDrivingsComponent } from './component/driving/show-drivings/show-drivings.component';
import { VerifyComponent } from './component/user/auth/verify/verify.component';
import { SendResetPasswordEmailComponent } from './component/user/auth/send-reset-password-link/send-reset-password-link.component';
import { ResetPasswordComponent } from './component/user/auth/reset-password/reset-password.component';
import { HomePageComponent } from './component/home/home-page/home-page.component';
import { ProfilePageComponent } from './component/user/profile-page/profile-page.component';
import { EditProfileComponent } from './component/user/edit-profile/edit-profile.component';
import { HistoryLiveChatComponent } from './component/chat/history-live-chat/history-live-chat.component';
import { ShowDriversComponent } from './component/admin/show-drivers/show-drivers.component';
import { ShowUsersComponent } from './component/admin/show-users/show-users.component';
import { MapComponent } from './component/map/map.component';
import { FavouriteRoutesComponent } from './component/favourite-route/favourite-routes/favourite-routes.component';
import { SuccessfullVerificationComponent } from './component/user/auth/successfull-verification/successfull-verification.component';
import { DrivingDetailsComponent } from './component/driving/driving-details-components/driving-details/driving-details.component';
import { BasicUserProfileComponent } from './component/user/basic-user-profile/basic-user-profile.component';

const routes: Routes = [
  { path: '', redirectTo: 'map-view/-1', pathMatch: 'full' },
  { path: 'map-view/:id', component: MapComponent },
  { path: 'login', component: LoginComponent },
  { path: 'registration', component: RegistrationComponent },
  { path: 'forgot-password', component: SendResetPasswordEmailComponent },
  { path: 'verify/:id', component: VerifyComponent },
  {
    path: 'successfull-verification',
    component: SuccessfullVerificationComponent,
  },
  { path: 'drivings/:id', component: ShowDrivingsComponent },
  { path: 'reset-password/:email', component: ResetPasswordComponent },
  { path: 'profile-page', component: ProfilePageComponent },
  { path: 'verify/:id', component: VerifyComponent },
  { path: 'reset-password/:email', component: ResetPasswordComponent },
  { path: 'profile-page', component: ProfilePageComponent },
  { path: 'edit-profile-data', component: EditProfileComponent },
  { path: 'messages', component: HistoryLiveChatComponent },
  { path: 'drivers', component: ShowDriversComponent },
  { path: 'users', component: ShowUsersComponent },
  { path: 'favourite-routes', component: FavouriteRoutesComponent },
  { path: 'user-profile/:id', component: BasicUserProfileComponent },
  { path: '', redirectTo: 'login', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
