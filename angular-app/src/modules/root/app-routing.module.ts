import {RouterModule, Routes} from "@angular/router";
import {NotFoundPageComponent} from "./pages/not-found-page/not-found-page.component";
import {CommonModule} from "@angular/common";
import {NgModule} from "@angular/core";
import {MaterialModule} from "../material/material.module";
import {RootLayoutComponent} from "./pages/root-layout/root-layout.component";

export const routes: Routes = [
  {
    path: "serb-uber",
    component: RootLayoutComponent,
    children: [
      {
        path: "auth",
        loadChildren: () =>
          import("./../auth/auth.module").then((m) => m.AuthModule),
      },
      {
        path: "user",
        loadChildren: () =>
          import("./../user/user.module").then((m) => m.UserModule),
      },
      {
        path: "regular-user",
        loadChildren: () =>
          import("./../regular_user/regular-user.module").then((m) => m.RegularUserModule),
      },
      {
        path: "admin",
        loadChildren: () =>
          import("./../admin/admin.module").then((m) => m.AdminModule),
      },
      {
        path: "",
        loadChildren: () =>
          import("./../shared/shared.module").then((m) => m.SharedModule),
      }]
  },
  {
    path: "",
    redirectTo: "/serb-uber/auth/login",
    pathMatch: "full",
  },
  { path: "**", component: NotFoundPageComponent }
];

@NgModule({
  imports: [CommonModule, RouterModule.forRoot(routes), MaterialModule],
  exports: [RouterModule],
  declarations: [],
})
export class AppRoutingModule {}
