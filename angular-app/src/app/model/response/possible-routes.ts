export class PossibleRoute{
  distance: number;
  timeInMin: number;
  pointList: LongLat[];

  constructor(distance: number, timeInMin: number, pointList: LongLat[]){
    this.distance = distance;
    this.timeInMin = timeInMin;
    this.pointList = pointList;
  }
}

class LongLat {
  lon: number;
  lat: number;

  constructor(lon: number, lat: number) {
    this.lon = lon;
    this.lat = lat;
  }
}
