import {Location} from "./response/location";

export class SearchingRoutesForm {
  location: Location;
  inputPlace: string = "";
  filteredPlaces;
  marker;
  constructor()

  constructor(
    location?: Location,
    inputPlace?: string,
    filteredPlaces?,
    marker?
  ){
    this.location = location;
    this.inputPlace = inputPlace;
    this.filteredPlaces = filteredPlaces;
    this.marker = marker;
  }
}
