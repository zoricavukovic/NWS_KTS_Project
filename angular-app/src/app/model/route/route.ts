import { DrivingLocation } from './driving-location';

export interface Route {
  locations: DrivingLocation[];
  distance: number;
  timeInMin: number;
  id?: number;
}
