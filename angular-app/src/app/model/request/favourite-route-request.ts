export class FavouriteRouteRequest{
    userEmail: string;
    routeId: number;

    constructor(userEmail: string, routeId: number){
        this.userEmail = userEmail;
        this.routeId = routeId;
    }
}