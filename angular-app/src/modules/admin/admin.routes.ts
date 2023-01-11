import {Routes} from "@angular/router";
import {UnauthUserGuard} from "../auth/guards/login/login.guard";
import {HistoryLiveChatComponent} from "./pages/history-live-chat/history-live-chat.component";
import {ChangePayingInfoComponent} from "./pages/change-paying-info/change-paying-info.component";
import {ShowDriversComponent} from "./pages/show-drivers/show-drivers.component";
import {ShowUsersComponent} from "./pages/show-users/show-users.component";

export const AdminRoutes: Routes = [
  {
    path: "messages",
    pathMatch: "full",
    component: HistoryLiveChatComponent,
    // canActivate: [UnauthUserGuard]
  },
  {
    path: "payment/change-paying-info",
    pathMatch: "full",
    component: ChangePayingInfoComponent,
    // canActivate: [UnauthUserGuard]
  },
  {
    path: 'drivers',
    pathMatch: "full",
    component: ShowDriversComponent
  },
  {
    path: 'users',
    pathMatch: "full",
    component: ShowUsersComponent
  },
  ]
