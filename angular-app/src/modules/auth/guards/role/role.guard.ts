import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router} from '@angular/router';
import {AuthService} from "../../services/auth-service/auth.service";
import {User} from "../../../shared/models/user/user";

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(public authService: AuthService, public  router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {

    const expectedRoles: string = route.data['expectedRoles'];
    const user: User = this.authService.getLoggedParsedUser();
    if (!user){
      this.router.navigate(["/serb-uber/auth/login"]);
      return false;
    }

    const roles: string[] = expectedRoles.split("|", 3);
    if (roles.indexOf(user.role.name) === -1){
      this.router.navigate(["/serb-uber/user/map-page-view/-1"]);
      return false;
    }

    return true;

  }

}
