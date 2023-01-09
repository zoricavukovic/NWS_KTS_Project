import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { PayingInfo } from '../model/payment/paying-info';
import { ConfigService } from './config.service';
import { GenericService } from './generic.service';

@Injectable({
  providedIn: 'root'
})
export class PayingInfoService extends GenericService<PayingInfo> {
  
  constructor(
    private http: HttpClient,
    private configService: ConfigService
    ) {
      super(http, `${configService.api_url}/paying-infos`);
    }

  createPayingInfoObj(tokenPrice: number, maxNumOfTokens: number): PayingInfo {
    
    return {
      tokenPrice: tokenPrice,
      maxNumOfTokensPerTransaction: maxNumOfTokens
    }
  }

}
