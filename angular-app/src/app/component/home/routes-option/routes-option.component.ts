import {Component, Input, OnChanges, OnDestroy, OnInit, SimpleChanges} from '@angular/core';
import {
  drawPolylineOnMap,
  getRouteCoordinates,
  removeAllPolyline,
  removeLine
} from "../../../util/map-functions";
import {PossibleRoutesViaPoints} from "../../../model/route/possible-routes-via-points";
import {ToastrService} from "ngx-toastr";
import {PossibleRoute} from "../../../model/route/possible-routes";
import {Route} from "../../../model/route/route";
import {DrivingLocation} from "../../../model/route/driving-location";
import {SearchingRoutesForm} from "../../../model/route/searching-routes-form";
import {Location} from "../../../model/route/location";

@Component({
  selector: 'app-routes-option',
  templateUrl: './routes-option.component.html',
  styleUrls: ['./routes-option.component.css']
})
export class RoutesOptionComponent implements OnChanges, OnDestroy {

  @Input() possibleRoutesViaPoints: PossibleRoutesViaPoints[];
  @Input() map: google.maps.Map;
  @Input() searchingRoutesForm: SearchingRoutesForm[];

  drawPolylineList: google.maps.Polyline[] = [];
  routePathIndex: number[] = [];

  responsiveOptions = [
    {
      breakpoint: '1024px',
      numVisible: 1,
      numScroll: 2,
    },
  ];

  constructor( private _toast: ToastrService) { }

  ngOnChanges(changes: SimpleChanges) {

    this.searchingRoutesForm = changes['searchingRoutesForm']?.currentValue;
    this.possibleRoutesViaPoints = changes['possibleRoutesViaPoints']?.currentValue;
    if (this.possibleRoutesViaPoints.length > 0) {
      this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
      this.changeCurrentRoutes(this.possibleRoutesViaPoints);
    } else {
      this._toast.error(
        'Cannot find routes for chosen places',
        'Unavailable routes'
      );
    }
  }

  changeCurrentRoutes(routes: PossibleRoutesViaPoints[]) {
    this.routePathIndex = [];
    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);

    if (routes.length === 1) {
      this.routePathIndex.push(0);

      routes.at(0).possibleRouteDTOList.forEach(oneRoute => {
        const routeCoordinates = getRouteCoordinates(oneRoute);
        this.drawPolyline(
          routes.at(0).possibleRouteDTOList.indexOf(oneRoute),
          0,
          routeCoordinates
        );
      });
    } else {
      routes.forEach(route => {
        this.routePathIndex.push(0);
        const routeCoordinates = getRouteCoordinates(
          route.possibleRouteDTOList.at(0)
        );
        this.drawPolylineList.push(
          drawPolylineOnMap(this.map, routeCoordinates, '#283b50', 9)
        );
      });
    }
  }

  private drawPolyline(
    indexOfSelectedPath: number,
    indexOfRouteInPossibleRoutes: number,
    latLongs: google.maps.LatLngLiteral[]
  ): void {
    const color: string = indexOfSelectedPath === 0 ? '#283b50' : '#cdd1d3';
    const weight: number = indexOfSelectedPath === 0 ? 9 : 7;
    const polyline: google.maps.Polyline = drawPolylineOnMap(
      this.map,
      latLongs,
      color,
      weight
    );
    polyline.setOptions({ clickable: true });
    this.drawPolylineList[indexOfSelectedPath] = polyline;
    const that = this;
    google.maps.event.addListener(polyline, 'click', function () {
      that.routePathIndex[indexOfRouteInPossibleRoutes] = indexOfSelectedPath;
      that.drawPolylineList.forEach(p => {
        p.setOptions({
          strokeColor: '#cdd1d3',
          strokeWeight: 7,
        });

        polyline.setOptions({
          strokeColor: '#283b50',
          strokeWeight: 9,
        });
      });
    });
  }

  getFromToLabel(index: number): string {
    const location: Location = this.searchingRoutesForm.at(index).location;

    return `${location.street} ${location.number}`;
  }

  changeOptionRouteOnClick(
    route: PossibleRoute,
    indexOfSelectedPath: number,
    indexOfRouteInPossibleRoutes: number
  ): void {
    this.routePathIndex[indexOfRouteInPossibleRoutes] = indexOfSelectedPath;

    if (this.hasOneDestination()) {
      this.swapColorsOfRoutes(indexOfSelectedPath);
    } else {
      this.removeOnePolyline(indexOfRouteInPossibleRoutes);
      const routeCoordinates = getRouteCoordinates(route);
      this.drawPolylineList[indexOfRouteInPossibleRoutes] = drawPolylineOnMap(
        this.map,
        routeCoordinates,
        '#283b50',
        9
      );
    }
  }

  private createListDrivingLocation(): DrivingLocation[] {
    const drivingLocations: DrivingLocation[] = [];
    this.searchingRoutesForm.forEach(searchingRoute => {
      drivingLocations.push({
        index: this.searchingRoutesForm.indexOf(searchingRoute) + 1,
        location: searchingRoute.location,
      });
    });

    return drivingLocations;
  }

  private createRoute(): Route {
    return {
      locations: this.createListDrivingLocation(),
      distance: this.calculateDistance(),
      timeInMin: this.calculateMinutes(),
      routePathIndex: this.routePathIndex,
    };
  }

  private calculateMinutes(): number {
    let minutes = 0;
    this.routePathIndex.forEach(index => {
      minutes += this.possibleRoutesViaPoints
        .at(this.routePathIndex.indexOf(index))
        .possibleRouteDTOList.at(index).timeInMin;
    });

    return minutes;
  }

  private calculateDistance(): number {
    let distance = 0;
    this.routePathIndex.forEach(index => {
      distance += this.possibleRoutesViaPoints
        .at(this.routePathIndex.indexOf(index))
        .possibleRouteDTOList.at(index).distance;
    });

    return distance;
  }

  private swapColorsOfRoutes(indexOfSelectedPath: number): void {
    this.drawPolylineList.forEach(p => {
      p.setOptions({
        strokeColor: '#cdd1d3',
        strokeWeight: 7,
      });
    });
    this.drawPolylineList.at(indexOfSelectedPath).setOptions({
      strokeColor: '#283b50',
      strokeWeight: 9,
    });
  }

  private hasOneDestination(): boolean {

    return this.searchingRoutesForm.length === 2;
  }

  private removeOnePolyline(index: number) {
    if (this.drawPolylineList.at(index)) {
      removeLine(this.drawPolylineList.at(index));
      this.drawPolylineList[index] = null;
    }
  }

  public chooseFastestRoute() {
    if (this.hasOneDestination()) {
      const minTimePath = this.possibleRoutesViaPoints
        .at(0)
        .possibleRouteDTOList.reduce((a, b) =>
          a.timeInMin < b.timeInMin ? a : b
        );
      const indexOfSelectedPath = this.possibleRoutesViaPoints
        .at(0)
        .possibleRouteDTOList.indexOf(minTimePath);
      this.routePathIndex[0] = indexOfSelectedPath;
      this.swapColorsOfRoutes(indexOfSelectedPath);
    } else {
      this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
      this.possibleRoutesViaPoints.forEach(route => {
        const minTimePath = route.possibleRouteDTOList.reduce((a, b) =>
          a.timeInMin < b.timeInMin ? a : b
        );
        const indexOfSelectedPath =
          route.possibleRouteDTOList.indexOf(minTimePath);
        this.routePathIndex[this.possibleRoutesViaPoints.indexOf(route)] =
          indexOfSelectedPath;
        const routeCoordinates = getRouteCoordinates(
          route.possibleRouteDTOList.at(indexOfSelectedPath)
        );
        this.drawPolylineList.push(
          drawPolylineOnMap(this.map, routeCoordinates, '#283b50', 9)
        );
      });
    }
  }

  public chooseShortestRoute() {
    if (this.hasOneDestination()) {
      const minTimePath = this.possibleRoutesViaPoints
        .at(0)
        .possibleRouteDTOList.reduce((a, b) =>
          a.distance < b.distance ? a : b
        );
      const indexOfSelectedPath = this.possibleRoutesViaPoints
        .at(0)
        .possibleRouteDTOList.indexOf(minTimePath);
      this.routePathIndex[0] = indexOfSelectedPath;
      this.swapColorsOfRoutes(indexOfSelectedPath);
    } else {
      this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
      this.possibleRoutesViaPoints.forEach(route => {
        const minDistancePath = route.possibleRouteDTOList.reduce((a, b) =>
          a.distance < b.distance ? a : b
        );
        const indexOfSelectedPath =
          route.possibleRouteDTOList.indexOf(minDistancePath);
        this.routePathIndex[this.possibleRoutesViaPoints.indexOf(route)] =
          indexOfSelectedPath;
        const routeCoordinates = getRouteCoordinates(
          route.possibleRouteDTOList.at(indexOfSelectedPath)
        );
        this.drawPolylineList.push(
          drawPolylineOnMap(this.map, routeCoordinates, '#283b50', 9)
        );
      });
    }
  }

  ngOnDestroy(): void {
    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
  }
}
