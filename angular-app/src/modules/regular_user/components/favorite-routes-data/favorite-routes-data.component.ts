import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { DrivingService } from 'src/modules/shared/services/driving-service/driving.service';
import { RegularUserService } from 'src/modules/shared/services/regular-user-service/regular-user.service';
import { Route } from '../../../shared/models/route/route';

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
    private regularUserService: RegularUserService,
    private router: Router,
    private drivingService: DrivingService
  ) {
    this.userId = 0;
    this.favoriteRoutes = null;
  }

  ngOnInit(): void {
    if (this.userId > 0) {
      this.favouriteRoutesSubscription = this.regularUserService
        .getFavouriteRoutesForUser(this.userId)
        .subscribe(res => {
          this.favoriteRoutes = res;
        });
    }
  }

  removeFromFavouriteRoutes(routeId: number) {
    this.regularUserService
      .updateFavouriteRoutes(
        this.regularUserService.createFavouriteRequest(this.userId, routeId)
      )
      .subscribe(response => {
        this.favoriteRoutes = this.favoriteRoutes.filter((item) => {
          return item.id !== routeId
        });
        console.log(response);
      });

      event.stopPropagation();
  }

  goToDetails(routeId: number) {
    this.drivingSubscription = this.drivingService.getDrivingByFavouriteRoute(routeId).subscribe(
      res => {
        this.router.navigate(["serb-uber/user/favourite-route", res]);
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
