import {Component, OnInit, Input, Output, EventEmitter, AfterViewInit} from '@angular/core';
import { Route } from 'src/app/model/route/route';
import { User } from 'src/app/model/user/user';
import { UserService } from 'src/app/service/user.service';
import { FavouriteRouteRequest } from 'src/app/model/route/favourite-route-request';
import * as L from 'leaflet';
import {drawPolyline} from "../../../util/map-functions";

@Component({
  selector: 'favourite-route-row',
  templateUrl: './favourite-route-row.component.html',
  styleUrls: ['./favourite-route-row.component.css'],
})
export class FavouriteRouteRowComponent implements OnInit, AfterViewInit {
  @Input() user: User;
  @Input() route: Route;
  @Input() index: number;
  @Output() removeFromFavourites = new EventEmitter();
  map: L.Map;
  startPoint: string;
  endPoint: string;

  constructor(private userService: UserService) {}

  ngAfterViewInit(): void {
    if (this.map) {
      drawPolyline(this.map, this.route);
    }
  }

  ngOnInit(): void {
    this.startPoint =`${this.route.locations.at(0).location.street}, ${this.route.locations.at(0).location.number}`;

    const end = this.route.locations.at(this.route.locations.length - 1);
    this.endPoint = `${end.location.street}, ${end.location.number}`;
    this.initMap();
  }



  initMap(){
    this.map = L.map('small-map').setView([45.25167, 19.83694], 13);

    L.tileLayer('//{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { crossOrigin: true}).addTo(this.map);

  }

  ngOnDestroy(): void {
    if (this.map !== undefined) { this.map = this.map.remove(); }
  }

  removeFromFavouriteRoutes() {
    console.log(this.route.id);
    this.userService
      .removeFromFavouriteRoutes(
        this.userService.createFavouriteRequest(this.user.id, this.route.id)
      )
      .subscribe(response => {
        console.log(response);
        this.removeFromFavourites.emit();
      });
  }
}
