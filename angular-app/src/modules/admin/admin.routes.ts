import {Routes} from "@angular/router";
import {HistoryLiveChatComponent} from "./pages/history-live-chat/history-live-chat.component";
import {ChangePayingInfoComponent} from "./pages/change-paying-info/change-paying-info.component";
import {ShowDriversComponent} from "./pages/show-drivers/show-drivers.component";
import {ShowUsersComponent} from "./pages/show-users/show-users.component";
import {RoleGuard} from "../auth/guards/role/role.guard";

export const AdminRoutes: Routes = [
  {
    path: "messages",
    pathMatch: "full",
    component: HistoryLiveChatComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_ADMIN"}
  },
  {
    path: "payment/change-paying-info",
    pathMatch: "full",
    component: ChangePayingInfoComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_ADMIN"}
  },
  {
    path: 'drivers',
    pathMatch: "full",
    component: ShowDriversComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_ADMIN"}
  },
  {
    path: 'users',
    pathMatch: "full",
    component: ShowUsersComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_ADMIN"}
  },
  ]
