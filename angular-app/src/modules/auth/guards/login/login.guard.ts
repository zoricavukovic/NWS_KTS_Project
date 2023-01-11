import { Injectable } from '@angular/core';
import {CanActivate, Router } from '@angular/router';
import {AuthService} from "../../services/auth-service/auth.service";

@Injectable({
  providedIn: 'root'
})
export class UnauthUserGuard implements CanActivate {
  constructor(public authService: AuthService, public router: Router) {}

  canActivate(): boolean {
    if (this.authService.getLoggedParsedUser()){
      this.router.navigate(["/serb-uber/user/map-page-view/-1"]);

      return false;
    }

    return true;
  }

}
