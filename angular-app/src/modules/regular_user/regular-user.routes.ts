import {Routes} from "@angular/router";
import {UnauthUserGuard} from "../auth/guards/login/login.guard";
import {FinancialCardComponent} from "./pages/financial-card/financial-card.component";
import {FavouriteRoutesComponent} from "./pages/favourite-routes/favourite-routes.component";
import {ProcessingPaymentComponent} from "./pages/processing-payment/processing-payment.component";
import {PaymentStatusComponent} from "./pages/payment-status/payment-status.component";
import {RegularUserRoleGuard} from "./guards/regular-user-role/regular-user-role.guard";

export const RegularUserRoutes: Routes = [
  {
    path:'favourite-routes',
    pathMatch: "full",
    component: FavouriteRoutesComponent,
    canActivate: [RegularUserRoleGuard],
    data: {expectedRoles: "ROLE_REGULAR_USER"}
  },
  {
    path:'payment/financial-card/:id',
    pathMatch: "full",
    component: FinancialCardComponent,
    // canActivate: [UnauthUserGuard]
  },
  {
    path:'payment/process-payment/:tokenBankId/:numOfTokens/process',
    pathMatch: "full",
    component: ProcessingPaymentComponent,
    // canActivate: [UnauthUserGuard]
  },
  {
    path:'payment/status/:status',
    pathMatch: "full",
    component: PaymentStatusComponent,
    // canActivate: [UnauthUserGuard]
  },
  ]
