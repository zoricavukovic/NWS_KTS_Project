import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/modules/auth/services/auth-service/auth.service';
import { DrivingService } from 'src/modules/shared/services/driving-service/driving.service';
import { Route } from '../../../shared/models/route/route';
import { UserService } from '../../../shared/services/user-service/user.service';

@Component({
  selector: 'app-favorite-routes-data',
  templateUrl: './favorite-routes-data.component.html',
  styleUrls: ['./favorite-routes-data.component.css']
})
export class FavoriteRoutesDataComponent implements OnInit, OnDestroy {

  @Input() userId: number;
  
  favouriteRoutesSubscription: Subscription;
  favoriteRoutes: Route[];

  drivingSubscription: Subscription;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private router: Router,
    private drivingService: DrivingService
  ) {
    this.userId = 0;
    this.favoriteRoutes = null;
  }

  ngOnInit(): void {
    if (this.userId > 0) {
      this.favouriteRoutesSubscription = this.authService
        .getFavouriteRoutesForUser(this.userId)
        .subscribe(res => {
          this.favoriteRoutes = res;
          console.log(this.favoriteRoutes)
        });
    }
  }

  removeFromFavouriteRoutes(routeId: number) {
    this.userService
      .updateFavouriteRoutes(
        this.userService.createFavouriteRequest(this.userId, routeId)
      )
      .subscribe(response => {
        this.favoriteRoutes = this.favoriteRoutes.filter((item) => {
          return item.id !== routeId
        })
      });
      
      event.stopPropagation();
  }

  goToDetails(routeId: number) {
    this.drivingSubscription = this.drivingService.getDrivingByFavouriteRoute(routeId).subscribe(
      res => {
        this.router.navigate([`/serb-uber/user/map-page-view/${res}`]);
      }
    );
  }

  ngOnDestroy(): void {
    if (this.favouriteRoutesSubscription) {
      this.favouriteRoutesSubscription.unsubscribe();
    }

    if (this.drivingSubscription) {
      this.drivingSubscription.unsubscribe();
    }
  }

}