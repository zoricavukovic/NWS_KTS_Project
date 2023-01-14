import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {MaterialModule} from "../material/material.module";
import {RouterModule} from "@angular/router";
import {SharedModule} from "../shared/shared.module";
import {RegularUserRoutes} from "./regular-user.routes";
import {FavouriteRoutesComponent} from "./pages/favourite-routes/favourite-routes.component";
import {FavouriteRouteRowComponent} from "./components/favourite-route-row/favourite-route-row.component";
import {HomePassangerComponent} from "./components/home-passanger/home-passanger.component";
import {AcceptingDrivingViewComponent} from "./components/accepting-driving-view/accepting-driving-view.component";
import {ActiveDriveContainerComponent} from "./components/active-drive-container/active-drive-container.component";
import {
  WaitingForAcceptRideContainerComponent
} from "./components/waiting-for-accept-ride-container/waiting-for-accept-ride-container.component";
import {FinancialCardComponent} from "./pages/financial-card/financial-card.component";
import {BuyTokensComponent} from "./components/payment/buy-tokens/buy-tokens.component";
import {PaymentStatusComponent} from "./pages/payment-status/payment-status.component";
import {ProcessingPaymentComponent} from "./pages/processing-payment/processing-payment.component";
import {TransactionsHistoryComponent} from "./components/payment/transactions-history/transactions-history.component";
import {CarouselModule} from "primeng/carousel";
import {ReactiveFormsModule} from "@angular/forms";
import {GoogleMapsModule} from "@angular/google-maps";
import {GooglePlaceModule} from "ngx-google-places-autocomplete";
import {ProgressSpinnerModule} from "primeng/progressspinner";
import {FilterVehicleViewComponent} from "./components/filter-vehicle-view/filter-vehicle-view.component";
import {RouteRowComponent} from "./components/route-row/route-row.component";
import { NgxsModule } from "@ngxs/store";
import { DrivingNotificationState } from "../shared/state/driving-notification.state";
import { NgxsReduxDevtoolsPluginModule } from "@ngxs/devtools-plugin";
import { NgxsLoggerPluginModule } from "@ngxs/logger-plugin";
import { FavoriteRoutesDataComponent } from "./components/favorite-routes-data/favorite-routes-data.component";

@NgModule({
  declarations: [
    FavouriteRoutesComponent,
    FavouriteRouteRowComponent,
    HomePassangerComponent,
    AcceptingDrivingViewComponent,
    ActiveDriveContainerComponent,
    WaitingForAcceptRideContainerComponent,
    FinancialCardComponent,
    BuyTokensComponent,
    PaymentStatusComponent,
    ProcessingPaymentComponent,
    TransactionsHistoryComponent,
    FilterVehicleViewComponent,
    RouteRowComponent,
    FavoriteRoutesDataComponent
  ],
  imports: [
    CommonModule,
    MaterialModule,
    RouterModule.forChild(RegularUserRoutes),
    SharedModule,
    CarouselModule,
    ReactiveFormsModule,
    GoogleMapsModule,
    GooglePlaceModule,
    ProgressSpinnerModule,
    // NgxsModule.forFeature([DrivingNotificationState]),
  ],
  exports: [HomePassangerComponent, FavoriteRoutesDataComponent],
  providers: []
})
export class RegularUserModule { }
