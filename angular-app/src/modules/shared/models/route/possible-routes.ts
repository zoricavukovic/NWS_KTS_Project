export interface PossibleRoute {
  distance: number;
  timeInMin: number;
  pointList: LongLat[];
  averagePrice: number;
}

interface LongLat {
  lon: number;
  lat: number;
}
