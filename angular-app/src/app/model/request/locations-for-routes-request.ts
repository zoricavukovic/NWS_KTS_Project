import {Location} from "../response/location";

export class LocationsForRoutesRequest{
  locationsForRouteRequest: Location[];

  constructor(locationsForRouteRequest: Location[]){
    this.locationsForRouteRequest = locationsForRouteRequest;
  }
}
