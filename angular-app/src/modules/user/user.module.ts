import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RouterModule} from "@angular/router";
import {UserRoutes} from "./user.routes";
import {MaterialModule} from "../material/material.module";
import {RegistrationComponent} from "./pages/registration/registration.component";
import {ResetPasswordComponent} from "./pages/reset-password/reset-password.component";
import {ProfilePageComponent} from "./pages/profile-page/profile-page.component";
import {EditProfileComponent} from "./pages/edit-profile/edit-profile.component";
import {BasicUserProfileComponent} from "./pages/basic-user-profile/basic-user-profile.component";
import {SharedModule} from "../shared/shared.module";
import {MapPageComponent} from "./pages/map-page/map-page.component";
import {HomeComponent} from "./components/home/home.component";
import {RegularUserModule} from "../regular_user/regular-user.module";
import {DriverModule} from "../driver/driver.module";
import {AdminModule} from "../admin/admin.module";

@NgModule({
  declarations: [
    RegistrationComponent,
    ResetPasswordComponent,
    ProfilePageComponent,
    EditProfileComponent,
    MapPageComponent,
    BasicUserProfileComponent,
    HomeComponent
  ],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    MaterialModule,
    RouterModule.forChild(UserRoutes),
    SharedModule,
    RegularUserModule,
    DriverModule,
    AdminModule,
  ],
  providers: []
})
export class UserModule { }
