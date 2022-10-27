import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { RegistrationResponse } from '../model/registration-reponse';
import { RegistrationRequest } from '../model/registration-request';
import { ConfigService } from './config.service';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class RegistrationService {

  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) { }

  register(registrationRequest: RegistrationRequest){
    console.log(registrationRequest);

    return this.http.post<RegistrationResponse>(this.configService.registration_url, registrationRequest)
    .pipe(
      map((response) => { 
          console.log(response);
        }
      )
  );
  }
}
