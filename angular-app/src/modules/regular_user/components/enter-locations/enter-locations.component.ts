import {Component, EventEmitter, Input, OnDestroy, Output} from '@angular/core';
import {ControlContainer, FormArray, FormBuilder, FormGroup} from "@angular/forms";
import {User} from "../../../shared/models/user/user";
import {Options} from "ngx-google-places-autocomplete/objects/options/options";
import {
  addMarker, calculateDistance, calculateMinutes,
  drawPolylineOnMap,
  getRouteCoordinates, removeAllMarkers,
  removeAllPolyline, removeLine,
  removeMarker
} from "../../../shared/utils/map-functions";
import {PossibleRoutesViaPoints} from "../../../shared/models/route/possible-routes-via-points";
import {SearchingRoutesForm} from "../../../shared/models/route/searching-routes-form";
import {ToastrService} from "ngx-toastr";
import {Address} from "ngx-google-places-autocomplete/objects/address";
import {Location} from "../../../shared/models/route/location";
import {RouteService} from "../../../shared/services/route-service/route.service";
import {Subscription} from "rxjs";
import {PossibleRoute} from "../../../shared/models/route/possible-routes";
import {Route} from "../../../shared/models/route/route";
import {DrivingLocation} from "../../../shared/models/route/driving-location";

@Component({
  selector: 'app-enter-locations',
  templateUrl: './enter-locations.component.html',
  styleUrls: ['./enter-locations.component.css']
})
export class EnterLocationsComponent implements OnDestroy {
  @Input() currentUser: User;
  @Input() map: google.maps.Map;

  @Output() clickedChooseVehicleAndPassengers = new EventEmitter();
  @Output() clickedChooseVehicleAndPassengersForLater = new EventEmitter();

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

  get searchingForm(){
    return this.rideRequestForm.get('searchingRoutesForm')['controls'];
  }

  deleteOneLocation(index: number) {
    this.deleteMarker(index);
    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
    (this.rideRequestForm.get('searchingRoutesForm') as FormArray).removeAt(index);
    if (this.checkIfDeletedLocationIsDestination(index)) {
      this.deleteMarker(index - 1);
      this.addMarkerWithLonAndLat(
        index - 1,
        this.searchingForm.at(index - 1).location.lon,
        this.searchingForm.at(index - 1).location.lat
      );
    }
    this.possibleRoutesViaPoints = [];
  }

  addOneMoreLocation() {
    this.possibleRoutesViaPoints = [];

    (this.rideRequestForm.get('searchingRoutesForm') as FormArray).insert(this.searchingForm.length - 1,
      this.createEmptySearchForm())

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
    (this.rideRequestForm.get('routePathIndex')?.value)[indexOfRouteInPossibleRoutes] = indexOfSelectedPath;

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

  getFromToLabel(index: number): string {
    const location: Location = this.searchingForm.at(index).value.location;

    return `${location.street} ${location.number}`;
  }

  createEmptySearchForm(): FormGroup{
    return this.formBuilder.group(new SearchingRoutesForm());
  }

  public addressChange(address: Address, index: number) {
    this.deleteMarker(index);
    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
    this.possibleRoutesViaPoints = [];
    if(address.formatted_address){

      (this.searchingForm).at(index).inputPlace = address.formatted_address;
      const lng: number = address.geometry.location.lng();
      const lat: number = address.geometry.location.lat();

      console.log(this.searchingForm);
      let marker: google.maps.Marker = addMarker(this.map, {
        lat: lat,
        lng: lng,
      });
      marker.setIcon(this.getIconUrl(index));
      const location = this.createLocation(address, index);
      this.searchingForm.at(index).setValue({
        inputPlace: address.formatted_address,
        "marker": marker,
        location: location
      });
    }
    else{
      this.toast.error('Please, choose suggested locations.', 'Invalid form');
      this.rideRequestForm.get('searchingRoutesForm').value.at(index).location = null;
    }
  }

  getIconName(index: number): string {
    switch (index) {
      case 1:
        return 'looks_one';
      case 2:
        return 'looks_two';
      default:
        return 'looks_' + index;
    }
  }

  private createLocation(address: Address, index: number): Location {
    let houseNumber = '';
    let city = '';
    let street = '';
    let zipCode = '';
    for (let i = 0; i < address.address_components.length; i++) {
      const mapAddress = address.address_components[i];
      if (mapAddress.long_name !== '') {
        if (mapAddress.types[0] === 'street_number') {
          houseNumber = mapAddress.long_name;
        }
        if (mapAddress.types[0] === 'route') {
          street = mapAddress.long_name;
        }

        if (mapAddress.types[0] === 'locality') {
          city = mapAddress.long_name;
        }
        if (mapAddress.types[0] === 'postal_code') {
          zipCode = mapAddress.long_name;
        }
      }
    }
    const lng: number = address.geometry.location.lng();
    const lat: number = address.geometry.location.lat();
    const location: Location = {
      city: city,
      lon: lng,
      lat: lat,
      street: street,
      number: houseNumber,
      zipCode: zipCode,
    };
    this.rideRequestForm.get('searchingRoutesForm').value.at(index).location = location;

    return location;
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
    }
    else{
      this.toast.error('Please, enter locations.', 'Invalid form');
    }
  }

  formIsInvalid(): boolean {
    let formIsInvalid = false;
    (this.searchingForm).forEach(searchingRoute => {
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
    this.clickedChooseVehicleAndPassengers.emit();
  }

  chooseVehicleAndPassengersLater(): void {
    this.rideRequestForm.get('selectedRoute')?.setValue(this.createRoute());
    this.clickedChooseVehicleAndPassengersForLater.emit();
  }

  private createRoute(): Route {
    return {
      locations: this.createListDrivingLocation(),
      distance: calculateDistance(this.rideRequestForm.get('routePathIndex').value, this.possibleRoutesViaPoints),
      timeInMin: calculateMinutes(this.rideRequestForm.get('routePathIndex').value, this.possibleRoutesViaPoints),
      routePathIndex: this.rideRequestForm.get('routePathIndex').value
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
      (this.rideRequestForm.get('routePathIndex')?.value)[0] = indexOfSelectedPath;
      this.swapColorsOfRoutes(indexOfSelectedPath);
    } else {
      this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
      this.possibleRoutesViaPoints.forEach(route => {
        const minTimePath = route.possibleRouteDTOList.reduce((a, b) =>
          a.timeInMin < b.timeInMin ? a : b
        );
        const indexOfSelectedPath =
          route.possibleRouteDTOList.indexOf(minTimePath);
        this.searchingForm[this.possibleRoutesViaPoints.indexOf(route)] =
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
      (this.rideRequestForm.get('routePathIndex')?.value)[0] = indexOfSelectedPath;
      this.swapColorsOfRoutes(indexOfSelectedPath);
    } else {
      this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
      this.possibleRoutesViaPoints.forEach(route => {
        const minDistancePath = route.possibleRouteDTOList.reduce((a, b) =>
          a.distance < b.distance ? a : b
        );
        const indexOfSelectedPath =
          route.possibleRouteDTOList.indexOf(minDistancePath);
        (this.rideRequestForm.get('routePathIndex')?.value)[this.possibleRoutesViaPoints.indexOf(route)] =
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

  changeCurrentRoutes(routes: PossibleRoutesViaPoints[]) {
    this.rideRequestForm.get('routePathIndex').setValue([]);
    this.drawPolylineList = removeAllPolyline(this.drawPolylineList);

    if (routes.length === 1) {
      this.rideRequestForm.get('routePathIndex')?.value.push(0);

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
      (that.rideRequestForm.get('routePathIndex')?.value)[indexOfRouteInPossibleRoutes] = indexOfSelectedPath;
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

  private hasOneDestination(): boolean {

    return this.searchingForm.length === 2;
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

  private getIconUrl(index: number): string {
    switch (index) {
      case 0:
        return '../../../assets/images/startMarker.png';
      case this.searchingForm.length - 1:
        return '../../../assets/images/endMarker.png';
      default:
        return '../../../assets/images/viaMarker.png';
    }
  }

  private deleteMarker(index: number) {
    if (this.searchingForm.at(index).value.marker){

      removeMarker(this.searchingForm.at(index).value.marker);
      // this.searchingForm.at(index).value.marker = null;
      this.drawPolylineList = removeAllPolyline(this.drawPolylineList);
    }
  }

  private checkIfDeletedLocationIsDestination(index: number) {
    return this.searchingForm.length === index;
  }

  private addMarkerWithLonAndLat(index: number, lon: number, lat: number) {
    const markerCoordinates: google.maps.LatLngLiteral = { lat: lat, lng: lon };
    (this.searchingForm).at(index).marker = addMarker(
      this.map,
      markerCoordinates
    );
    (this.searchingForm).at(index).marker.setIcon(this.getIconUrl(index));
  }

  ngOnDestroy(): void {

    if (this.routeSubscription) {
      this.routeSubscription.unsubscribe();
    }
  }
}
