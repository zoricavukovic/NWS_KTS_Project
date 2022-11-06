import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'
import { LoginComponent } from './component/login/login.component';
import { RegistrationComponent } from './component/registration/registration.component';
import { VerifyComponent } from './component/verify/verify.component';
import { SendResetPasswordEmailComponent } from './component/send-reset-password-link/send-reset-password-link.component';
import { ResetPasswordComponent } from './component/reset-password/reset-password.component';
import { ProfilePageComponent } from './component/profile-page/profile-page.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path: 'forgot-password', component: SendResetPasswordEmailComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'profile-page', component: ProfilePageComponent},
  {path: 'verify/:id', component: VerifyComponent},
  {path: 'reset-password/:email', component: ResetPasswordComponent},
  {path:'', redirectTo:'login', pathMatch:'full'}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
