import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {InAppSpending, TokenBank} from "../../models/payment/token-bank";
import {ConfigService} from "../config-service/config.service";
import {GenericService} from "../generic-service/generic.service";

@Injectable({
  providedIn: 'root'
})
export class TokenBankService  extends GenericService<TokenBank>{

  constructor(
    private http: HttpClient,
    private configService: ConfigService
    ) {
      super(http, configService.TOKEN_BANKS_URL);
    }

    getInAppSpending(): Observable<InAppSpending> {
      return this.http.get<InAppSpending>(
        this.configService.SPENDING_TOKENS_URL
      );
    }
}
