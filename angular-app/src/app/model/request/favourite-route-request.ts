export class FavouriteRouteRequest {
  userId: number;
  routeId: number;

  constructor(userId: number, routeId: number) {
    this.userId = userId;
    this.routeId = routeId;
  }
}
