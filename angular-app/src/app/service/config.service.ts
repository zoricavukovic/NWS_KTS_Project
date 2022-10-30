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
  private _register_user = this._api_url + "/regularUsers/register";
  private _verify_url = this._api_url + "/verify"
  private _send_verify_code_again = this._api_url + "/verify/send-code-again"

  get login_url(): string {
    return this._login_user;
  }


  get registration_url(): string{
    return this._register_user;
  }

  get verify_url(): string {
    return this._verify_url;
  }

  get send_verify_code_url(): string {
    return this._send_verify_code_again;
  }

}
