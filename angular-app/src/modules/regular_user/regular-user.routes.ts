import {Routes} from "@angular/router";
import {FinancialCardComponent} from "./pages/financial-card/financial-card.component";
import {FavouriteRoutesComponent} from "./pages/favourite-routes/favourite-routes.component";
import {ProcessingPaymentComponent} from "./pages/processing-payment/processing-payment.component";
import {PaymentStatusComponent} from "./pages/payment-status/payment-status.component";
import {RoleGuard} from "../auth/guards/role/role.guard";

export const RegularUserRoutes: Routes = [
  {
    path:'favourite-routes',
    pathMatch: "full",
    component: FavouriteRoutesComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_REGULAR_USER"}
  },
  {
    path:'payment/financial-card/:id',
    pathMatch: "full",
    component: FinancialCardComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_REGULAR_USER"}
  },
  {
    path:'payment/process-payment/:tokenBankId/:numOfTokens/process',
    pathMatch: "full",
    component: ProcessingPaymentComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_REGULAR_USER"}
  },
  {
    path:'payment/status/:status',
    pathMatch: "full",
    component: PaymentStatusComponent,
    canActivate: [RoleGuard],
    data: {expectedRoles: "ROLE_REGULAR_USER"}
  }
  ]
