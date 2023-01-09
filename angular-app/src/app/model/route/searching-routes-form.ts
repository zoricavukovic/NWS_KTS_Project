import { Location } from './location';

export class SearchingRoutesForm {
  location: Location;
  inputPlace: string;
  marker: google.maps.Marker;
  constructor();

  constructor(
    location?: Location,
    inputPlace?: string,
    marker?:google.maps.Marker
  ) {
    this.location = location;
    this.inputPlace = inputPlace;
    this.marker = marker;
  }
}
