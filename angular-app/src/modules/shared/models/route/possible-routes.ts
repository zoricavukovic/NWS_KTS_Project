export interface PossibleRoute {
  distance: number;
  timeInMin: number;
  pointList: LongLat[];
}

interface LongLat {
  lon: number;
  lat: number;
}
