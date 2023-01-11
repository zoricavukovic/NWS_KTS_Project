import {CommonModule} from "@angular/common";
import {CustomInterceptor} from "./interceptors/custom.interceptor";
import {HttpClientModule, HTTP_INTERCEPTORS} from "@angular/common/http";
import {NgModule} from "@angular/core";
import {MaterialModule} from "../material/material.module";
import {PaymentStatisticsComponent} from "./components/payment-statistics/payment-statistics.component";
import {RouterModule} from "@angular/router";
import {ToastrModule} from "ngx-toastr";
import {SharedRoutes} from "./shared.routes";
import {NgxsModule} from "@ngxs/store";
import {NgxsReduxDevtoolsPluginModule} from "@ngxs/devtools-plugin";
import {NgxsLoggerPluginModule} from "@ngxs/logger-plugin";
import {KnobModule} from "primeng/knob";
import {DrivingNotificationState} from "./state/driving-notification.state";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ChangeProfilePicComponent} from "./components/change-profile-pic/change-profile-pic.component";
import {BasicUserDataComponent} from "./components/basic-user-data/basic-user-data.component";
import {ButtonLiveChatComponent} from "./components/chat/button-live-chat/button-live-chat.component";
import {PoupupLiveChatComponent} from "./components/chat/poupup-live-chat/poupup-live-chat.component";
import {ConfirmBlockingDialogComponent} from "./components/confirm-blocking-dialog/confirm-blocking-dialog.component";
import {
  DrivingDetailsComponent
} from "./components/driving-details-components/driving-details/driving-details.component";
import {
  DrivingDetailsActionsComponent
} from "./components/driving-details-components/driving-details-actions/driving-details-actions.component";
import {
  DrivingDetailsDriverComponent
} from "./components/driving-details-components/driving-details-driver/driving-details-driver.component";
import {
  DrivingDetailsPassengersComponent
} from "./components/driving-details-components/driving-details-passengers/driving-details-passengers.component";
import {
  DrivingDetailsRouteComponent
} from "./components/driving-details-components/driving-details-route/driving-details-route.component";
import {DrivingRowComponent} from "./components/driving-row/driving-row.component";
import {RatingDialogComponent} from "./components/rating-dialog/rating-dialog.component";
import {ReviewsHistoryComponent} from "./components/reviews-history/reviews-history.component";
import {UserAverageRateComponent} from "./components/user-average-rate/user-average-rate.component";
import {UtilMessageComponent} from "./components/util-message/util-message.component";
import {DriverVehicleComponent} from "./components/vehicle/create-vehicle/driver-vehicle.component";
import {VehicleDetailsComponent} from "./components/vehicle/vehicle-details/vehicle-details.component";
import {ShowDrivingsComponent} from "./pages/show-drivings/show-drivings.component";
import { NgxStarsModule } from 'ngx-stars';
import { GoogleMapsModule } from '@angular/google-maps';
import { GooglePlaceModule } from 'ngx-google-places-autocomplete';
import {CarouselModule} from "primeng/carousel";

@NgModule({
  declarations: [
    PaymentStatisticsComponent,
    BasicUserDataComponent,
    ChangeProfilePicComponent,
    ButtonLiveChatComponent,
    PoupupLiveChatComponent,
    ConfirmBlockingDialogComponent,
    DrivingDetailsComponent,
    DrivingDetailsActionsComponent,
    DrivingDetailsDriverComponent,
    DrivingDetailsPassengersComponent,
    DrivingDetailsRouteComponent,
    DrivingRowComponent,
    RatingDialogComponent,
    ReviewsHistoryComponent,
    UserAverageRateComponent,
    UtilMessageComponent,
    DriverVehicleComponent,
    VehicleDetailsComponent,
    ShowDrivingsComponent
    // PaginationComponent,
  ],
  imports: [
    CommonModule,
    MaterialModule,
    FormsModule,
    HttpClientModule,
    RouterModule.forChild(SharedRoutes),
    ToastrModule.forRoot({
      positionClass: 'toast-bottom-left',
      preventDuplicates: true,
      closeButton: true,
    }),
    NgxsModule.forRoot([
      DrivingNotificationState
    ]),
    NgxsReduxDevtoolsPluginModule.forRoot(),
    NgxsLoggerPluginModule.forRoot(),
    KnobModule,
    NgxStarsModule,
    GoogleMapsModule,
    GooglePlaceModule,
    CarouselModule,
    ReactiveFormsModule
  ],
  exports: [
    PaymentStatisticsComponent,
    ButtonLiveChatComponent,
    UtilMessageComponent,
    DrivingDetailsRouteComponent,
    VehicleDetailsComponent,
    DrivingDetailsPassengersComponent,
    DriverVehicleComponent,
    DrivingDetailsComponent,
    ReviewsHistoryComponent,
    BasicUserDataComponent,
    UserAverageRateComponent
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: CustomInterceptor, multi: true },
  ],
})
export class SharedModule {}
