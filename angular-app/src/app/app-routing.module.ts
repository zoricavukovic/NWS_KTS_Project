import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'
import { DrivingDetailsComponent } from './component/driving-details/driving-details.component';
import { LoginComponent } from './component/login/login.component';
import { RegistrationComponent } from './component/registration/registration.component';
import { ShowDrivingsComponent } from './component/show-drivings/show-drivings.component';
import { VerifyComponent } from './component/verify/verify.component';
import { SendResetPasswordEmailComponent } from './component/send-reset-password-link/send-reset-password-link.component';
import { ResetPasswordComponent } from './component/reset-password/reset-password.component';
import { HomePageComponent } from './component/home-page/home-page.component';
import { ProfilePageComponent } from './component/profile-page/profile-page.component';
import { EditProfileComponent } from './component/edit-profile/edit-profile.component';

const routes: Routes = [
  {path:'', redirectTo:'home-page', pathMatch:'full'},
  {path:'home-page', component: HomePageComponent},
  {path: 'login', component: LoginComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'forgot-password', component: SendResetPasswordEmailComponent},
  {path: 'verify/:id', component: VerifyComponent},
  {path: 'drivings', component: ShowDrivingsComponent},
  {path: 'details/:id', component: DrivingDetailsComponent},
  {path: 'reset-password/:email', component: ResetPasswordComponent},
  {path: 'profile-page', component: ProfilePageComponent},
  {path: 'verify/:id', component: VerifyComponent},
  {path: 'reset-password/:email', component: ResetPasswordComponent},
  {path: 'profile-page', component: ProfilePageComponent},
  {path: 'edit-profile-data', component: EditProfileComponent},
  {path:'', redirectTo:'login', pathMatch:'full'}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
