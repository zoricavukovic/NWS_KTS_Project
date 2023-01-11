import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import {ConfigService} from "../../shared/services/config-service/config.service";
import {User} from "../../shared/models/user/user";
import {VerifyRequest} from "../../shared/models/user/verify-request";

@Injectable({
  providedIn: 'root',
})
export class VerifyService {
  constructor(
    private http: HttpClient,
    private configService: ConfigService
  ) {}

  verify(verifyRequest: VerifyRequest): Observable<User> {
    return this.http.put<User>(this.configService.VERIFY_URL, verifyRequest);
  }

  sendCodeAgain(verifyId: number): Observable<User> {
    return this.http.post<User>(
      this.configService.SEND_CODE_AGAIN_URL,
      verifyId
    );
  }

  createVerifyRequest(verifyId: number, securityCode: number): VerifyRequest {
    return {
      verifyId: verifyId,
      securityCode: securityCode,
    };
  }
}
