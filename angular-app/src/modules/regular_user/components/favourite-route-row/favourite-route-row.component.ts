import {
  Component,
  OnInit,
  Input,
  Output,
  EventEmitter,
  AfterViewInit,
  OnDestroy,
} from '@angular/core';
import {User} from "../../../shared/models/user/user";
import {UserService} from "../../../shared/services/user-service/user.service";
import {RouteService} from "../../../shared/services/route-service/route.service";
import {Route} from "../../../shared/models/route/route";
import {drawPolylineWithLngLatArray} from "../../../shared/utils/map-functions";

@Component({
  selector: 'favourite-route-row',
  templateUrl: './favourite-route-row.component.html',
  styleUrls: ['./favourite-route-row.component.css'],
})
export class FavouriteRouteRowComponent
  implements OnInit, OnDestroy, AfterViewInit
{
  @Input() user: User;
  @Input() route: Route;
  @Input() index: number;
  @Output() removeFromFavourites = new EventEmitter();
  map: google.maps.Map;
  center: google.maps.LatLngLiteral = { lat: 45.25167, lng: 19.83694 };
  zoom = 13;
  startPoint: string;
  endPoint: string;

  constructor(private userService: UserService, private routeService: RouteService) {}

  ngAfterViewInit(): void {
    if (this.map){
      this.routeService.getRoutePath(this.route?.id).subscribe(path =>
        drawPolylineWithLngLatArray(this.map, path)
      )
    }
  }

  ngOnInit(): void {
    this.startPoint = `${this.route.locations.at(0).location.street}, ${
      this.route.locations.at(0).location.number
    }`;

    const end = this.route.locations.at(this.route.locations.length - 1);
    this.endPoint = `${end.location.street}, ${end.location.number}`;
    this.initMap();
  }

  initMap() {
    this.map = new google.maps.Map(
      document.getElementById('small-map') as HTMLElement,
      {
        center: this.center,
        zoom: this.zoom,
        zoomControl: true,
        zoomControlOptions: {
          position: google.maps.ControlPosition.TOP_RIGHT,
        },
        streetViewControl: false,
        mapTypeControl: false,
      }
    );
  }

  ngOnDestroy(): void {
    if (this.map !== undefined) {
      this.map = null;
    }
  }

  removeFromFavouriteRoutes() {
    this.userService
      .updateFavouriteRoutes(
        this.userService.createFavouriteRequest(this.user.id, this.route.id)
      )
      .subscribe(response => {
        this.removeFromFavourites.emit();
      });
  }
}
