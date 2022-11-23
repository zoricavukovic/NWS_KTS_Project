import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ConfigService } from './config.service';
import { VerifyRequest } from '../model/user/verify-request';
import { map } from 'rxjs/operators';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from '../model/user/user';

@Injectable({
  providedIn: 'root',
})
export class VerifyService {
  constructor(
    private http: HttpClient,
    private configService: ConfigService,
    private router: Router
  ) {}

  verify(verifyRequest: VerifyRequest): Observable<User> {
    return this.http.put<User>(this.configService.verify_url, verifyRequest);
  }

  sendCodeAgain(verifyId: Number): Observable<User> {
    return this.http.post<User>(
      this.configService.send_verify_code_url,
      verifyId
    );
  }

  createVerifyRequest(
    verifyId: number,
    securityCode: number,
    userRole: string
  ): VerifyRequest {
    return {
      verifyId: verifyId,
      securityCode: securityCode,
      userRole: userRole,
    };
  }
}
