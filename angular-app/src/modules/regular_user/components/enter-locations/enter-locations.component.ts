import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  Output,
} from '@angular/core';
import {
  ControlContainer,
  FormArray,
  FormBuilder,
  FormGroup,
} from '@angular/forms';
import { User } from '../../../shared/models/user/user';
import { Options } from 'ngx-google-places-autocomplete/objects/options/options';
import {
  addMarkerWithCustomIcon,
  addMarkerWithLonLat,
  calculateDistance,
  calculateMinutes,
  createLocationFromAutocomplete,
  drawPolylineOnMap,
  drawPolylineWithClickOption,
  getRouteCoordinates,
  removeAllPolyline,
  removeLine,
  removeMarkerAndAllPolyline,
} from '../../../shared/utils/map-functions';
import { PossibleRoutesViaPoints } from '../../../shared/models/route/possible-routes-via-points';
import { ToastrService } from 'ngx-toastr';
import { Address } from 'ngx-google-places-autocomplete/objects/address';
import { Location } from '../../../shared/models/route/location';
import { RouteService } from '../../../shared/services/route-service/route.service';
import { Subscription } from 'rxjs';
import { PossibleRoute } from '../../../shared/models/route/possible-routes';
import { Route } from '../../../shared/models/route/route';
import { DrivingLocation } from '../../../shared/models/route/driving-location';
import { swapColorsOfRoutes } from '../../../shared/utils/color-change';
import {createEmptySearchForm} from "../../../shared/utils/form-helper";

@Component({
  selector: 'app-enter-locations',
  templateUrl: './enter-locations.component.html',
  styleUrls: ['./enter-locations.component.css'],
})
export class EnterLocationsComponent implements OnDestroy {
  @Input() currentUser: User;
  @Input() map: google.maps.Map;

  @Output() clickedChooseVehicleAndPassengers = new EventEmitter<
    google.maps.Polyline[]
  >();
  @Output() clickedChooseVehicleAndPassengersForLater = new EventEmitter<
    google.maps.Polyline[]
  >();

  rideRequestForm: FormGroup;
  maxNumberOfLocations = 5;

  options: Options = new Options({
    bounds: undefined,
    fields: ['address_component', 'formatted_address', 'name', 'geometry'],
    strictBounds: false,
    componentRestrictions: { country: 'rs' },
  });

  responsiveOptions = [
    {
      breakpoint: '1024px',
      numVisible: 1,
      numScroll: 2,
    },
  ];

  possibleRoutesViaPoints: PossibleRoutesViaPoints[] = [];
  drawPolylineList: google.maps.Polyline[] = [];

  routeSubscription: Subscription;

  constructor(
    private controlContainer: ControlContainer,
    private formBuilder: FormBuilder,
    private toast: ToastrService,
    private routeService: RouteService
  ) {
    this.rideRequestForm = <FormGroup>this.controlContainer.control;
  }

  get searchingForm() {
    return this.rideRequestForm.get('searchingRoutesForm')['controls'];
  }

  deleteOneLocation(index: number) {
    this.drawPolylineList = removeMarkerAndAllPolyline(
      this.drawPolylineList,
      this.searchingForm.at(index)
    );
    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
    (this.rideRequestForm.get('searchingRoutesForm') as FormArray).removeAt(
      index
    );

    if (this.checkIfDeletedLocationIsDestination(index)) {
      if (index === 0) {
        index = 0;
      } else {
        index = index - 1;
      }
      this.drawPolylineList = removeMarkerAndAllPolyline(
        this.drawPolylineList,
        this.searchingForm.at(index)
      );

      this.searchingForm.at(index).marker = addMarkerWithLonLat(
        this.searchingForm.at(index).value.location?.lat,
        this.searchingForm.at(index).value.location?.lon,
        this.map,
        index,
        this.searchingForm
      );
    }

    this.possibleRoutesViaPoints = [];
  }

  addOneMoreLocation() {
    this.possibleRoutesViaPoints = [];

    (this.rideRequestForm.get('searchingRoutesForm') as FormArray).insert(
      this.searchingForm.length - 1,
      createEmptySearchForm(this.formBuilder)
    );
  }

  canAddMoreLocation() {
    return (
      this.searchingForm.length < this.maxNumberOfLocations &&
      this.currentUser !== null &&
      this.currentUser !== undefined
    );
  }

  changeOptionRouteOnClick(
    route: PossibleRoute,
    indexOfSelectedPath: number,
    indexOfRouteInPossibleRoutes: number
  ): void {
    if (this.rideRequestForm.get('routePathIndex')?.value) {
      this.rideRequestForm.get('routePathIndex').value[
        indexOfRouteInPossibleRoutes
      ] = indexOfSelectedPath;
    }

    if (this.hasOneDestination()) {
      swapColorsOfRoutes(indexOfSelectedPath, this.drawPolylineList);
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

  getFromToLabel(index: number): string {
    const location: Location = this.searchingForm.at(index).value.location;

    return `${location.street} ${location.number}`;
  }

  public addressChange(address: Address, index: number) {
    this.drawPolylineList = removeMarkerAndAllPolyline(
      this.drawPolylineList,
      this.searchingForm.at(index)
    );
    this.possibleRoutesViaPoints = [];
    if (address.formatted_address) {
      this.searchingForm.at(index).inputPlace = address.formatted_address;
      const lng: number = address.geometry.location.lng();
      const lat: number = address.geometry.location.lat();

      const marker: google.maps.Marker = addMarkerWithCustomIcon(
        lat,
        lng,
        index,
        this.map,
        this.searchingForm
      );
      const location = createLocationFromAutocomplete(
        address,
        index,
        this.rideRequestForm
      );
      this.searchingForm.at(index).setValue({
        inputPlace: address.formatted_address,
        marker: marker,
        location: location,
      });
    } else {
      this.toast.error('Please, choose suggested locations.', 'Invalid form');
      this.rideRequestForm.get('searchingRoutesForm').value.at(index).location =
        null;
    }
  }

  getMatIconName(index: number): string {
    switch (index) {
      case 1:
        return 'looks_one';
      case 2:
        return 'looks_two';
      default:
        return 'looks_' + index;
    }
  }

  getPossibleRoutes() {
    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
    if (!this.formIsInvalid()) {
      const locationsForCreateRoutes: Location[] = [];

      this.searchingForm.forEach(searchingRoutesLocation =>
        locationsForCreateRoutes.push(searchingRoutesLocation.value.location)
      );

      this.routeSubscription = this.routeService
        .getPossibleRoutes(
          this.routeService.createLocationForRoutesRequest(
            locationsForCreateRoutes
          )
        )
        .subscribe(res => {
          this.possibleRoutesViaPoints = res;
          if (res.length > 0) {
            this.changeCurrentRoutes(res);
          } else {
            this.toast.error(
              'Cannot find routes for chosen places',
              'Unavailable routes'
            );
          }
        });
    } else {
      this.toast.error('Please, enter locations.', 'Invalid form');
    }
  }

  formIsInvalid(): boolean {
    let formIsInvalid = false;
    this.searchingForm.forEach(searchingRoute => {
      if (
        searchingRoute.value.location === undefined ||
        searchingRoute.value.location === null
      ) {
        formIsInvalid = true;
      }
    });

    return formIsInvalid;
  }

  chooseVehicleAndPassengers(): void {
    this.rideRequestForm.get('selectedRoute')?.setValue(this.createRoute());
    this.clickedChooseVehicleAndPassengers.emit(this.drawPolylineList);
  }

  chooseVehicleAndPassengersLater(): void {
    this.rideRequestForm.get('selectedRoute')?.setValue(this.createRoute());
    this.clickedChooseVehicleAndPassengersForLater.emit(this.drawPolylineList);
  }

  private createRoute(): Route {
    return {
      locations: this.createListDrivingLocation(),
      distance: calculateDistance(
        this.rideRequestForm.get('routePathIndex').value,
        this.possibleRoutesViaPoints
      ),
      timeInMin: calculateMinutes(
        this.rideRequestForm.get('routePathIndex').value,
        this.possibleRoutesViaPoints
      ),
      routePathIndex: this.rideRequestForm.get('routePathIndex').value,
    };
  }

  private createListDrivingLocation(): DrivingLocation[] {
    const drivingLocations: DrivingLocation[] = [];
    this.searchingForm.forEach(searchingRoute => {
      drivingLocations.push({
        index: this.searchingForm.indexOf(searchingRoute) + 1,
        location: searchingRoute.value.location,
      });
    });

    return drivingLocations;
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
      if (this.rideRequestForm.get('routePathIndex')?.value) {
        this.rideRequestForm.get('routePathIndex').value[0] =
          indexOfSelectedPath;
      }
      swapColorsOfRoutes(indexOfSelectedPath, this.drawPolylineList);
    } else {
      this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
      this.possibleRoutesViaPoints.forEach(route => {
        const minTimePath = route.possibleRouteDTOList.reduce((a, b) =>
          a.timeInMin < b.timeInMin ? a : b
        );
        const indexOfSelectedPath =
          route.possibleRouteDTOList.indexOf(minTimePath);
        this.rideRequestForm.get('routePathIndex').value[
          this.possibleRoutesViaPoints.indexOf(route)
        ] = indexOfSelectedPath;
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
      if (this.rideRequestForm.get('routePathIndex')?.value) {
        this.rideRequestForm.get('routePathIndex').value[0] =
          indexOfSelectedPath;
      }
      swapColorsOfRoutes(indexOfSelectedPath, this.drawPolylineList);
    } else {
      this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
      this.possibleRoutesViaPoints.forEach(route => {
        const minDistancePath = route.possibleRouteDTOList.reduce((a, b) =>
          a.distance < b.distance ? a : b
        );
        const indexOfSelectedPath =
          route.possibleRouteDTOList.indexOf(minDistancePath);
        if (this.rideRequestForm.get('routePathIndex')?.value) {
          this.rideRequestForm.get('routePathIndex').value[
            this.possibleRoutesViaPoints.indexOf(route)
          ] = indexOfSelectedPath;
        }
        const routeCoordinates = getRouteCoordinates(
          route.possibleRouteDTOList.at(indexOfSelectedPath)
        );
        this.drawPolylineList.push(
          drawPolylineOnMap(this.map, routeCoordinates, '#283b50', 9)
        );
      });
    }
  }

  changeCurrentRoutes(routes: PossibleRoutesViaPoints[]) {
    this.rideRequestForm.get('routePathIndex').setValue([]);
    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);

    if (routes.length === 1) {
      this.rideRequestForm.get('routePathIndex')?.value.push(0);

      routes.at(0).possibleRouteDTOList.forEach(oneRoute => {
        const routeCoordinates = getRouteCoordinates(oneRoute);
        drawPolylineWithClickOption(
          routes.at(0).possibleRouteDTOList.indexOf(oneRoute),
          0,
          routeCoordinates,
          this.drawPolylineList,
          this.map,
          this.rideRequestForm
        );
      });
    } else {
      routes.forEach(route => {
        this.rideRequestForm.get('routePathIndex')?.value.push(0);
        const routeCoordinates = getRouteCoordinates(
          route.possibleRouteDTOList.at(0)
        );
        this.drawPolylineList.push(
          drawPolylineOnMap(this.map, routeCoordinates, '#283b50', 9)
        );
      });
    }
  }

  private removeOnePolyline(index: number) {
    if (this.drawPolylineList.at(index)) {
      removeLine(this.drawPolylineList.at(index));
      this.drawPolylineList[index] = null;
    }
  }

  ngOnDestroy(): void {
    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }

  private hasOneDestination(): boolean {
    return this.searchingForm.length === 2;
  }

  private checkIfDeletedLocationIsDestination(index: number) {
    return this.searchingForm.length === index || index === 0;
  }
}
