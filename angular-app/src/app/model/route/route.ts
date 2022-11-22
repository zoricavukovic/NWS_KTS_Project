import { DrivingLocation } from './driving-location';

export interface Route {
  id: number;
  locations: DrivingLocation[];
  distance: number;
  timeInMin: number;
}
