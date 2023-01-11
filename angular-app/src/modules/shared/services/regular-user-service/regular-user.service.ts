import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {GenericService} from "../generic-service/generic.service";
import {ConfigService} from "../config-service/config.service";
import {RegularUser} from "../../models/user/regular-user";

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
}
