import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router} from '@angular/router';
import {User} from "../../../shared/models/user/user";
import {AuthService} from "../../../auth/services/auth-service/auth.service";

@Injectable({
  providedIn: 'root'
})
export class AdminRoleGuard implements CanActivate {

  constructor(public authService: AuthService, public  router: Router) {}

  canActivate(route: ActivatedRouteSnapshot): boolean {

    const expectedRoles: string = route.data['expectedRoles'];
    const user: User = this.authService.getLoggedParsedUser();
    if (!user){
      this.router.navigate(["/serb-uber/auth/login"]);
      return false;
    }

    if (expectedRoles !== user.role.name){
      this.router.navigate(["/serb-uber/user/map-page-view/-1"]);
      return false;
    }

    return true;

  }

}
