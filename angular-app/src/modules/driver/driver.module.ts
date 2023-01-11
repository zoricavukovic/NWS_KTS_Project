import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {MaterialModule} from "../material/material.module";
import {SharedModule} from "../shared/shared.module";
import {
  DriverHomePageContainerComponent
} from "./components/driver-home-page-container/driver-home-page-container.component";
import {RejectDrivingComponent} from "./components/reject-driving/reject-driving.component";
import {
  SimpleDrivingDetailContainerComponent
} from "./components/simple-driving-detail-container/simple-driving-detail-container.component";
import {AvatarModule} from "primeng/avatar";
import {AvatarGroupModule} from "primeng/avatargroup";
import {FormsModule} from "@angular/forms";

@NgModule({
    declarations: [
        DriverHomePageContainerComponent,
        RejectDrivingComponent,
        SimpleDrivingDetailContainerComponent
    ],
    imports: [
        CommonModule,
        MaterialModule,
        FormsModule,
        SharedModule,
        AvatarModule,
        AvatarGroupModule
    ],
    exports: [
        DriverHomePageContainerComponent
    ],
    providers: []
})
export class DriverModule { }
