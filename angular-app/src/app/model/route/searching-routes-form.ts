import { Location } from './location';

export class SearchingRoutesForm {
  location: Location;
  inputPlace: string;
  filteredPlaces;
  marker: google.maps.Marker;
  constructor();

  constructor(
    location?: Location,
    inputPlace?: string,
    filteredPlaces?,
    marker?
  ) {
    this.location = location;
    this.inputPlace = inputPlace;
    this.filteredPlaces = filteredPlaces;
    this.marker = marker;
  }
}
