import {Routes} from "@angular/router";
import {RegistrationComponent} from "./pages/registration/registration.component";
import {ResetPasswordComponent} from "./pages/reset-password/reset-password.component";
import {RegistrationGuard} from "./guards/registration/registration.guard";
import {ProfilePageComponent} from "./pages/profile-page/profile-page.component";
import {EditProfileComponent} from "./pages/edit-profile/edit-profile.component";
import {UnauthUserGuard} from "../auth/guards/login/login.guard";
import {BasicUserProfileComponent} from "./pages/basic-user-profile/basic-user-profile.component";
import {RoleGuard} from "../auth/guards/role/role.guard";
import {MapPageComponent} from "./pages/map-page/map-page.component";


export const UserRoutes: Routes = [
  {
    path: "registration",
    pathMatch: "full",
    component: RegistrationComponent,
    canActivate: [RegistrationGuard],
    data: {expectedRoles: "ROLE_ADMIN"}
  },
  {
    path: 'map-page-view/:id',
    pathMatch: "full",
    component: MapPageComponent
  },
  {
    path: 'driving/:id',
    pathMatch: "full",
    component: MapPageComponent
  },
  {
    path: "reset-password/:email",
    pathMatch: "full",
    component: ResetPasswordComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_ADMIN|ROLE_REGULAR_USER|ROLE_DRIVER"}
  },
  {
    path: 'profile-page',
    pathMatch: "full",
    component: ProfilePageComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_ADMIN|ROLE_REGULAR_USER|ROLE_DRIVER"}
  },
  {
    path: 'edit-profile-data',
    pathMatch: "full",
    component: EditProfileComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_ADMIN|ROLE_REGULAR_USER|ROLE_DRIVER"}
  },
  {
    path: "user-profile/:id",
    pathMatch: "full",
    component: BasicUserProfileComponent,
    // canActivate: [UnauthUserGuard]
  }
]