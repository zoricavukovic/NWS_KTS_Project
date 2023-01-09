import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { InAppSpending, TokenBank } from '../model/payment/token-bank';
import { ConfigService } from './config.service';
import { GenericService } from './generic.service';

@Injectable({
  providedIn: 'root'
})
export class TokenBankService  extends GenericService<TokenBank>{

  constructor(
    private http: HttpClient,
    private configService: ConfigService
    ) {
      super(http, `${configService.api_url}/token-banks`);
    }

    getInAppSpending(): Observable<InAppSpending> {
      return this.http.get<InAppSpending>(
        this.configService.in_app_spending
      );
    }
}
