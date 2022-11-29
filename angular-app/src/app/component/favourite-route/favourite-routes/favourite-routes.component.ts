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
          console.log(this.favouriteRoutes);
          this.favouriteRoutes = data;
          console.log(this.favouriteRoutes);
      });
      }
    );
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
    if (this.favouriteRoutesSubscription) {
      this.favouriteRoutesSubscription.unsubscribe();
    }

    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}
