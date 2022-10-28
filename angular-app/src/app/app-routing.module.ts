import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'
import { LoginComponent } from './component/login/login.component';
import { RegistrationComponent } from './component/registration/registration.component';
import { VerifyComponent } from './component/verify/verify.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path:'', redirectTo:'login', pathMatch:'full'},
  {path: 'registration', component: RegistrationComponent},
  {path: 'verify', component: VerifyComponent},
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
