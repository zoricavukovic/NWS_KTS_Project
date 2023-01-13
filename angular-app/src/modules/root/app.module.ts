import {AppComponent} from "./app.component";
import {NgModule} from "@angular/core";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {CommonModule} from "@angular/common";
import {BrowserModule} from "@angular/platform-browser";
import {NavBarComponent} from "./components/nav-bar/nav-bar.component";
import {NotFoundPageComponent} from "./pages/not-found-page/not-found-page.component";
import {AuthModule} from "../auth/auth.module";
import {AppRoutingModule} from "./app-routing.module";
import {SharedModule} from "../shared/shared.module";
import {MaterialModule} from "../material/material.module";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {RootLayoutComponent} from "./pages/root-layout/root-layout.component";
import { NgxsModule } from "@ngxs/store";
import { NgxsReduxDevtoolsPluginModule } from "@ngxs/devtools-plugin";
import { NgxsLoggerPluginModule } from "@ngxs/logger-plugin";
import { DrivingNotificationState } from "../shared/state/driving-notification.state";
import { NgxsStoragePluginModule } from "@ngxs/storage-plugin";

@NgModule({
  declarations: [
    AppComponent,
    NavBarComponent,
    NotFoundPageComponent,
    RootLayoutComponent
  ],
  imports: [
    BrowserAnimationsModule,
    CommonModule,
    ReactiveFormsModule,
    FormsModule,
    BrowserModule,
    SharedModule,
    AuthModule,
    AppRoutingModule,
    MaterialModule,
    NgxsModule.forRoot([
      DrivingNotificationState
    ]),
    NgxsReduxDevtoolsPluginModule.forRoot(),
    NgxsLoggerPluginModule.forRoot(),
    NgxsStoragePluginModule.forRoot()
  ],
  providers: [],
  bootstrap: [AppComponent],
})
export class AppModule {}
