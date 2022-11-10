import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'
import { DrivingDetailsComponent } from './component/driving/driving-details/driving-details.component';
import { LoginComponent } from './component/user/auth/login/login.component';
import { RegistrationComponent } from './component/user/auth/registration/registration.component';
import { ShowDrivingsComponent } from './component/driving/show-drivings/show-drivings.component';
import { VerifyComponent } from './component/user/auth/verify/verify.component';
import { SendResetPasswordEmailComponent } from './component/user/auth/send-reset-password-link/send-reset-password-link.component';
import { ResetPasswordComponent } from './component/user/auth/reset-password/reset-password.component';
import { HomePageComponent } from './component/home-page/home-page.component';
import { ProfilePageComponent } from './component/user/profile-page/profile-page.component';
import { EditProfileComponent } from './component/user/edit-profile/edit-profile.component';
import { ShowDriversComponent } from './component/admin/show-drivers/show-drivers.component';
import { ShowUsersComponent } from './component/admin/show-users/show-users.component';

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
  {path: 'drivers', component: ShowDriversComponent},
  {path: 'users', component: ShowUsersComponent},
  {path:'', redirectTo:'login', pathMatch:'full'}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
