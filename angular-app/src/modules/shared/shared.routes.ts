import {Routes} from "@angular/router";
import {ShowDrivingsComponent} from "./pages/show-drivings/show-drivings.component";
import {RoleGuard} from "../auth/guards/role/role.guard";

export const SharedRoutes: Routes = [
  {
    path: "drivings/:id",
    pathMatch: "full",
    component: ShowDrivingsComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_ADMIN|ROLE_DRIVER|ROLE_REGULAR_USER"}
  }
]
