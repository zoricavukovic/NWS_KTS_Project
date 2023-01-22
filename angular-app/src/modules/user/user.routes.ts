import {Routes} from "@angular/router";
import {RegistrationComponent} from "./pages/registration/registration.component";
import {ResetPasswordComponent} from "./pages/reset-password/reset-password.component";
import {RegistrationGuard} from "./guards/registration/registration.guard";
import {EditProfileComponent} from "./pages/edit-profile/edit-profile.component";
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
    component: MapPageComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_REGULAR_USER|ROLE_DRIVER"}
  },
  {
    path: 'favourite-route/:id',
    pathMatch: "full",
    component: MapPageComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_REGULAR_USER"}
  },
  {
    path: 'driving-notification/:id',
    pathMatch: "full",
    component: MapPageComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_REGULAR_USER"}
  },
  {
    path: "reset-password/:email",
    pathMatch: "full",
    component: ResetPasswordComponent
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
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_ADMIN|ROLE_REGULAR_USER|ROLE_DRIVER"}
  },
  {
    path: "user-profile/:id/:status",
    pathMatch: "full",
    component: BasicUserProfileComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_ADMIN|ROLE_REGULAR_USER|ROLE_DRIVER"}
  }
]
