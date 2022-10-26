import { HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ConfigService {

  header = new HttpHeaders().set("Authorization", localStorage.getItem('token'));

  private _api_url = environment.apiUrl;
  private _login_user = this._api_url + "/auth/login";

  get login_url(): string {
    return this._login_user;
  }

}
