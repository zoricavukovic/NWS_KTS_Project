import {Routes} from "@angular/router";
import {LoginComponent} from "./pages/login/login.component";
import {UnauthUserGuard} from "./guards/login/login.guard";
import {SendResetPasswordEmailComponent} from "./pages/send-reset-password-link/send-reset-password-link.component";
import {VerifyComponent} from "./pages/verify/verify.component";
import {SuccessfullVerificationComponent} from "./pages/successfull-verification/successfull-verification.component";

export const AuthRoutes: Routes = [
  {
    path: "login",
    pathMatch: "full",
    component: LoginComponent,
    canActivate: [UnauthUserGuard]
  },
  {
    path: "verify/:id",
    component: VerifyComponent,
    canActivate: [UnauthUserGuard]
  },
  {
    path: "forgot-password",
    component: SendResetPasswordEmailComponent,
    canActivate: [UnauthUserGuard]
  },
  {
    path: "successfull-verification",
    component: SuccessfullVerificationComponent,
    canActivate: [UnauthUserGuard]
  }
]
