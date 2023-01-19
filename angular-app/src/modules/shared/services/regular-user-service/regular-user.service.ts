import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {GenericService} from "../generic-service/generic.service";
import {ConfigService} from "../config-service/config.service";
import {RegularUser} from "../../models/user/regular-user";
import { FavouriteRouteRequest } from '../../models/route/favourite-route-request';
import { Route } from '../../../shared/models/route/route';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class RegularUserService extends GenericService<RegularUser> {
  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    super(http, configService.REGULAR_USERS_URL);
  }

  getFavouriteRoutesForUser(userId: number): Observable<Route[]> {
    return this.http.get<Route[]>(
      this.configService.allFavouriteRoutesUrl(userId)
    );
  }

  addToFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest) {

    return this.http.post<boolean>(this.configService.SET_FAVOURITE_ROUTE_URL, favouriteRouteRequest);
  }

  updateFavouriteRoutes(favouriteRouteRequest: FavouriteRouteRequest) {
    return this.http.post<boolean>(
      this.configService.SET_FAVOURITE_ROUTE_URL,
      favouriteRouteRequest
    );
  }

  isFavouriteRouteForUser(routeId: number, userId: number) {
    return this.http.get(
      this.configService.isFavouriteRouteUrl(routeId, userId)
    );
  }

  createFavouriteRequest(
    userId: number,
    routeId: number
  ): FavouriteRouteRequest {
    return {
      userId: userId,
      routeId: routeId,
    };
  }
}
