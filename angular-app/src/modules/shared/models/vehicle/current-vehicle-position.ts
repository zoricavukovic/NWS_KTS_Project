import {VehicleCurrentLocation} from "./vehicle-current-location";

export interface CurrentVehiclePosition {
  vehicleCurrentLocation: VehicleCurrentLocation;
  marker: google.maps.Marker;
}
