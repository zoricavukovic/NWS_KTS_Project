import {Routes} from "@angular/router";
import {UnauthUserGuard} from "../auth/guards/login/login.guard";
import { DrivingDetailsComponent } from "./pages/driving-details/driving-details.component";
import {ShowDrivingsComponent} from "./pages/show-drivings/show-drivings.component";

export const SharedRoutes: Routes = [
  {
    path: "drivings/:id",
    pathMatch: "full",
    component: ShowDrivingsComponent,
    // canActivate: [UnauthUserGuard]
  }
]
