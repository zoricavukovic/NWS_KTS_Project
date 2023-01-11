import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {MaterialModule} from "../material/material.module";
import {RouterModule} from "@angular/router";
import {AdminRoutes} from "./admin.routes";
import {ChatRoomsListComponent} from "./components/chat-rooms-list/chat-rooms-list.component";
import {ChangePayingInfoComponent} from "./pages/change-paying-info/change-paying-info.component";
import {HistoryLiveChatComponent} from "./pages/history-live-chat/history-live-chat.component";
import {ShowDriversComponent} from "./pages/show-drivers/show-drivers.component";
import {ShowUsersComponent} from "./pages/show-users/show-users.component";
import {DriverRowComponent} from "./components/driver-row/driver-row.component";
import {MessagesBoxComponent} from "./components/messages-box/messages-box.component";
import {PayingInfoBoxComponent} from "./components/paying-info-box/paying-info-box.component";
import {ReportsHistoryComponent} from "./components/reports-history/reports-history.component";
import {UserRowComponent} from "./components/user-row/user-row.component";
import {NgxStarsModule} from "ngx-stars";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {CarouselModule} from "primeng/carousel";
import {SharedModule} from "../shared/shared.module";

@NgModule({
  declarations: [
    ChangePayingInfoComponent,
    HistoryLiveChatComponent,
    ShowDriversComponent,
    ShowUsersComponent,
    ChatRoomsListComponent,
    DriverRowComponent,
    MessagesBoxComponent,
    PayingInfoBoxComponent,
    ReportsHistoryComponent,
    UserRowComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    NgxStarsModule,
    FormsModule,
    ReactiveFormsModule,
    CarouselModule,
    RouterModule.forChild(AdminRoutes),
    SharedModule
  ],
  exports: [
    ReportsHistoryComponent
  ],
  providers: []
})
export class AdminModule { }
