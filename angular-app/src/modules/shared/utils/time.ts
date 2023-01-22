import {DrivingNotification} from "../models/notification/driving-notification";
import {calculateTimeToDestination} from "./map-functions";
import {CurrentVehiclePosition} from "../models/vehicle/current-vehicle-position";
import {Store} from "@ngxs/store";

function moreThanMinute(storedDrivingNotification: DrivingNotification): boolean {

  return storedDrivingNotification.minutes >= 1;
}

export function getTime(storedDrivingNotification: DrivingNotification): string {
  let value = '0';
  if (storedDrivingNotification){
    if (moreThanMinute(storedDrivingNotification)){
      value = (storedDrivingNotification.minutes)
        ?.toFixed(1);
      return `${value}min`;
    }
    value = (storedDrivingNotification.minutes*60)
      ?.toFixed(1);
  }

  return `${value}sec`;
}

export function updateTime(
  storedDrivingNotification: DrivingNotification,
  vehicle: CurrentVehiclePosition,
  directionService: google.maps.DirectionsService,
  store: Store,
  iterator: number
): number {
  if (storedDrivingNotification && storedDrivingNotification?.vehicleId === vehicle.vehicleCurrentLocation.id){
    if (vehicle.vehicleCurrentLocation.inDrive) {
      if (iterator >= 10) {
        calculateTimeToDestination(vehicle, storedDrivingNotification.route, directionService, store);
        iterator = 0;
      } else if (this.iterator === 0) {
        calculateTimeToDestination(vehicle, storedDrivingNotification.route, directionService, store);
      }
    }

  }

  return iterator;
}
