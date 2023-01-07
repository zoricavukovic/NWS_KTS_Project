import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegularUser } from '../model/user/regular-user';
import { ConfigService } from './config.service';
import { GenericService } from './generic.service';

@Injectable({
  providedIn: 'root',
})
export class RegularUserService extends GenericService<RegularUser> {
  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {
    super(http, `${configService.api_url}/regular-users`);
  }
}
