import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { VerifyRequest } from '../model/request/verify-request';
import { map } from 'rxjs/operators';
import { Router } from '@angular/router';


@Injectable({
  providedIn: 'root'
})
export class VerifyService {

  constructor(
    private http: HttpClient, 
    private configService: ConfigService,
    private router: Router
  ) { }

  verify(verifyRequest: VerifyRequest) {
    return this.http.put<any>(this.configService.verify_url, verifyRequest)
      .pipe(
          map((response) => { 
              console.log(response);
              this.router.navigate(['/login'])
            }
          )
      );
  }

  sendCodeAgain(verifyId: string) {
    console.log(verifyId);
    return this.http.post<any>(this.configService.send_verify_code_url, verifyId)
      .pipe(
          map((response) => { 
              console.log(response);
            }
          )
      );
  }

}
