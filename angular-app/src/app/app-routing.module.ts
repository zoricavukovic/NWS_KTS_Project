import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'
import { LoginComponent } from './component/login/login.component';
import { RegistrationComponent } from './component/registration/registration.component';
import { VerifyComponent } from './component/verify/verify.component';
import { SendResetPasswordEmailComponent } from './component/send-reset-password-link/send-reset-password-link.component';
import { ResetPasswordComponent } from './component/reset-password/reset-password.component';
import { HomePageComponent } from './component/home-page/home-page.component';

const routes: Routes = [
  {path:'', redirectTo:'home-page', pathMatch:'full'},
  {path:'home-page', component: HomePageComponent},
  {path: 'login', component: LoginComponent},
  {path: 'registration', component: RegistrationComponent},
  {path: 'forgot-password', component: SendResetPasswordEmailComponent},
  {path: 'verify/:id', component: VerifyComponent},
  {path: 'reset-password/:email', component: ResetPasswordComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
