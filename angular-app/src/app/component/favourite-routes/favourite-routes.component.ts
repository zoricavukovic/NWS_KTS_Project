import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/response/user/user';
import { Route } from 'src/app/model/response/route';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-favourite-routes',
  templateUrl: './favourite-routes.component.html',
  styleUrls: ['./favourite-routes.component.css']
})
export class FavouriteRoutesComponent implements OnInit, OnDestroy {

  currentUser: User;
  favouriteRoutes: Route[];

  currentUserSubscription: Subscription;
  favouriteRoutesSubscription: Subscription;

  constructor(private authService: AuthService) { }

  ngOnInit(): void {
    this.currentUserSubscription = this.authService.getCurrentUser().subscribe(
      (data) => {
        this.currentUser=data;
        this.favouriteRoutesSubscription = this.authService.getFavouriteRoutesForUser(this.currentUser.email).subscribe(
          (data) => {
            this.favouriteRoutes = data;
          }
        )
      });
  }

  removeFromFavourites(){
    this.favouriteRoutesSubscription = this.authService.getFavouriteRoutesForUser(this.currentUser.email).subscribe(
      (data) => {
        this.favouriteRoutes = data;
      }
    )
  }

  ngOnDestroy(): void {
    if(this.currentUserSubscription){
      this.currentUserSubscription.unsubscribe();
    }

    if(this.favouriteRoutesSubscription){
      this.favouriteRoutesSubscription.unsubscribe();
    }
  }

}
