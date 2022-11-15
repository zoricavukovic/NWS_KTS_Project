import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { Route } from 'src/app/model/response/route';
import { User } from 'src/app/model/response/user/user';
import { UserService } from 'src/app/service/user.service';
import { FavouriteRouteRequest } from 'src/app/model/request/favourite-route-request';

@Component({
  selector: 'favourite-route-row',
  templateUrl: './favourite-route-row.component.html',
  styleUrls: ['./favourite-route-row.component.css'],
})
export class FavouriteRouteRowComponent implements OnInit {
  @Input() user: User;
  @Input() route: Route;
  @Input() index: number;
  @Output() removeFromFavourites = new EventEmitter();

  startPoint: string;
  endPoint: string;

  constructor(private userService: UserService) {}

  ngOnInit(): void {
    this.startPoint = this.route.locations.at(0).street + this.route.locations.at(0).number;
    let end = this.route.locations.at(this.route.locations.length - 1);
    this.endPoint = end.street + end.number;
  }

  removeFromFavouriteRoutes() {
    this.userService
      .removeFromFavouriteRoutes(
        new FavouriteRouteRequest(this.user.email, this.route.id)
      )
      .subscribe(response => {
        console.log(response);
        this.removeFromFavourites.emit();
      });
  }
}
