import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import {User} from "../../../shared/models/user/user";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {Route} from "../../../shared/models/route/route";

@Component({
  selector: 'app-favourite-routes',
  templateUrl: './favourite-routes.component.html',
  styleUrls: ['./favourite-routes.component.css'],
})
export class FavouriteRoutesComponent implements OnInit, OnDestroy {
  currentUser: User;
  favouriteRoutes: Route[] = [];

  favouriteRoutesSubscription: Subscription;
  authSubscription: Subscription;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.authSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        this.currentUser = user;
        this.favouriteRoutesSubscription = this.authService
        .getFavouriteRoutesForUser(user?.id)
        .subscribe(data => {
          this.favouriteRoutes = data;
      });
      }
    );
  }

  removeFromFavourites() {
    this.favouriteRoutesSubscription = this.authService
      .getFavouriteRoutesForUser(this.currentUser.id)
      .subscribe(data => {
        this.favouriteRoutes = data;
      });
  }

  ngOnDestroy(): void {
    if (this.favouriteRoutesSubscription) {
      this.favouriteRoutesSubscription.unsubscribe();
    }

    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
