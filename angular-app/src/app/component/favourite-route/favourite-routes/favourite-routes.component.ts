import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/user/user';
import { Route } from 'src/app/model/route/route';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-favourite-routes',
  templateUrl: './favourite-routes.component.html',
  styleUrls: ['./favourite-routes.component.css'],
})
export class FavouriteRoutesComponent implements OnInit, OnDestroy {
  currentUser: User;
  favouriteRoutes: Route[] = [];

  currentUserSubscription: Subscription;
  favouriteRoutesSubscription: Subscription;

  constructor(private authService: AuthService) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser;
    this.favouriteRoutesSubscription = this.authService
      .getFavouriteRoutesForUser(this.currentUser.id)
      .subscribe(data => {
        console.log(this.favouriteRoutes);
        this.favouriteRoutes = data;
        console.log(this.favouriteRoutes);
      });
  }

  removeFromFavourites() {
    this.favouriteRoutesSubscription = this.authService
      .getFavouriteRoutesForUser(this.currentUser.id)
      .subscribe(data => {
        this.favouriteRoutes = data;
        console.log(this.favouriteRoutes);
      });
  }

  ngOnDestroy(): void {
    if (this.currentUserSubscription) {
      this.currentUserSubscription.unsubscribe();
    }

    if (this.favouriteRoutesSubscription) {
      this.favouriteRoutesSubscription.unsubscribe();
    }
  }
}
