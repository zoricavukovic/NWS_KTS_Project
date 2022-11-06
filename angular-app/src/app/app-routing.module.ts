import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router'
import { DrivingDetailsComponent } from './component/driving-details/driving-details.component';
import { LoginComponent } from './component/login/login.component';
import { RegistrationComponent } from './component/registration/registration.component';
import { ShowDrivingsComponent } from './component/show-drivings/show-drivings.component';
import { VerifyComponent } from './component/verify/verify.component';

const routes: Routes = [
  {path: 'login', component: LoginComponent},
  {path:'', redirectTo:'login', pathMatch:'full'},
  {path: 'registration', component: RegistrationComponent},
  {path: 'verify/:id', component: VerifyComponent},
  {path: 'drivings', component: ShowDrivingsComponent},
  {path: 'details/:id', component: DrivingDetailsComponent}
];


@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
