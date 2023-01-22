import {CommonModule} from "@angular/common";
import {CustomInterceptor} from "./interceptors/custom.interceptor";
import {HttpClientModule, HTTP_INTERCEPTORS} from "@angular/common/http";
import {NgModule} from "@angular/core";
import {MaterialModule} from "../material/material.module";
import {PaymentStatisticsComponent} from "./components/payment-statistics/payment-statistics.component";
import {RouterModule} from "@angular/router";
import {ToastrModule} from "ngx-toastr";
import {SharedRoutes} from "./shared.routes";
import {KnobModule} from "primeng/knob";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {ChangeProfilePicComponent} from "./components/change-profile-pic/change-profile-pic.component";
import {BasicUserDataComponent} from "./components/basic-user-data/basic-user-data.component";
import {ButtonLiveChatComponent} from "./components/chat/button-live-chat/button-live-chat.component";
import {PoupupLiveChatComponent} from "./components/chat/poupup-live-chat/poupup-live-chat.component";
import {ConfirmBlockingDialogComponent} from "./components/confirm-blocking-dialog/confirm-blocking-dialog.component";
import {
  DrivingDetailsComponent
} from "./pages/driving-details/driving-details.component";
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
import {UtilMessageComponent} from "./components/util-message/util-message.component";
import {DriverVehicleComponent} from "./components/vehicle/create-vehicle/driver-vehicle.component";
import {VehicleDetailsComponent} from "./components/vehicle/vehicle-details/vehicle-details.component";
import {ShowDrivingsComponent} from "./pages/show-drivings/show-drivings.component";
import { NgxStarsModule } from 'ngx-stars';
import { GoogleMapsModule } from '@angular/google-maps';
import { GooglePlaceModule } from 'ngx-google-places-autocomplete';
import {CarouselModule} from "primeng/carousel";
import { PaginationComponent } from './components/pagination/pagination.component';
import {DateFormatPipe} from "./pipes/date-format.pipe";
import {AvatarModule} from "primeng/avatar";
import {AvatarGroupModule} from "primeng/avatargroup";
import { VehicleRateDataComponent } from './components/vehicle/vehicle-rate-data/vehicle-rate-data.component';
import { ReviewRowComponent } from './components/review-row/review-row.component';
import { UserProfileInfoComponent } from "./components/user-profile-info/user-profile-info.component";
import { ReportsPageComponent } from './pages/reports-page/reports-page.component';
import { ReportStatisticsComponent } from './components/reports/report-statistics/report-statistics.component';
import { ReportGraphComponent } from './components/reports/report-graph/report-graph.component';
import { ReportTabComponent } from './components/reports/report-tab/report-tab.component';
import { DatePipe } from '@angular/common';
import { BehaviourReportDialogComponent } from './components/behaviour-report-dialog/behaviour-report-dialog.component';
import { FilterVehicleViewComponent } from "./components/filter-vehicle-view/filter-vehicle-view.component";
import { RequestLaterTimeComponent } from "./components/request-later-time/request-later-time.component";
import { WaitingForAcceptRideContainerComponent } from "./components/waiting-for-accept-ride-container/waiting-for-accept-ride-container.component";

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
        UtilMessageComponent,
        DriverVehicleComponent,
        VehicleDetailsComponent,
        ShowDrivingsComponent,
        PaginationComponent,
        DateFormatPipe,
        VehicleRateDataComponent,
        ReviewRowComponent,
        UserProfileInfoComponent,
        ReportsPageComponent,
        ReportStatisticsComponent,
        ReportGraphComponent,
        ReportTabComponent,
        FilterVehicleViewComponent,
        RequestLaterTimeComponent,
        WaitingForAcceptRideContainerComponent,
        BehaviourReportDialogComponent
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
        PaginationComponent,
        DateFormatPipe,
        UserProfileInfoComponent,
        FilterVehicleViewComponent,
        RequestLaterTimeComponent,
        WaitingForAcceptRideContainerComponent
    ],
    providers: [
        { provide: HTTP_INTERCEPTORS, useClass: CustomInterceptor, multi: true },
        DatePipe
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
        KnobModule,
        NgxStarsModule,
        GoogleMapsModule,
        GooglePlaceModule,
        CarouselModule,
        ReactiveFormsModule,
        AvatarModule,
        AvatarGroupModule
    ]
})
export class SharedModule {}
