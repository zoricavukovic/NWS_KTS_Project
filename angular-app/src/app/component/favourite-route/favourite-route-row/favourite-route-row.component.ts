import {Component, OnInit, Input, Output, EventEmitter, AfterViewInit, OnDestroy} from '@angular/core';
import { Route } from 'src/app/model/route/route';
import { User } from 'src/app/model/user/user';
import { UserService } from 'src/app/service/user.service';
import {drawPolylineOnMapHaveRoute} from "../../../util/map-functions";

@Component({
  selector: 'favourite-route-row',
  templateUrl: './favourite-route-row.component.html',
  styleUrls: ['./favourite-route-row.component.css'],
})
export class FavouriteRouteRowComponent implements OnInit, OnDestroy, AfterViewInit {
  @Input() user: User;
  @Input() route: Route;
  @Input() index: number;
  @Output() removeFromFavourites = new EventEmitter();
  map: google.maps.Map;
  center: google.maps.LatLngLiteral = {lat: 45.25167, lng: 19.83694};
  zoom= 13;
  startPoint: string;
  endPoint: string;

  constructor(private userService: UserService) {}

  ngAfterViewInit(): void {
    if (this.map) {
      drawPolylineOnMapHaveRoute(this.map, this.route);
    }
  }

  ngOnInit(): void {
    this.startPoint =`${this.route.locations.at(0).location.street}, ${this.route.locations.at(0).location.number}`;

    const end = this.route.locations.at(this.route.locations.length - 1);
    this.endPoint = `${end.location.street}, ${end.location.number}`;
    this.initMap();
  }

  initMap(){
    this.map = new google.maps.Map(document.getElementById("small-map") as HTMLElement, {
      center: this.center,
      zoom: this.zoom,
      zoomControl: true,
      zoomControlOptions: {
        position: google.maps.ControlPosition.TOP_RIGHT,
      },
      streetViewControl: false,
      mapTypeControl: false
    });
  }

  ngOnDestroy(): void {
    if (this.map !== undefined) { this.map = null; }
  }

  removeFromFavouriteRoutes() {
    this.userService
      .removeFromFavouriteRoutes(
        this.userService.createFavouriteRequest(this.user.id, this.route.id)
      )
      .subscribe(response => {
        this.removeFromFavourites.emit();
      });
  }
}
