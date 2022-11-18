import { DrivingLocation } from './driving-location';

export class Route {
  id: number;
  locations: DrivingLocation[];
  distance: number;
  timeInMin: number;
}
