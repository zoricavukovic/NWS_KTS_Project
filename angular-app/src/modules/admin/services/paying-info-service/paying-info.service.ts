import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import {PayingInfo} from "../../../shared/models/payment/paying-info";
import {ConfigService} from "../../../shared/services/config-service/config.service";
import {GenericService} from "../../../shared/services/generic-service/generic.service";

@Injectable({
  providedIn: 'root'
})
export class PayingInfoService extends GenericService<PayingInfo> {

  constructor(
    private http: HttpClient,
    private configService: ConfigService
    ) {
      super(http, configService.PAYING_INFOS_URL);
    }

  createPayingInfoObj(tokenPrice: number, maxNumOfTokens: number): PayingInfo {

    return {
      tokenPrice: tokenPrice,
      maxNumOfTokensPerTransaction: maxNumOfTokens
    }
  }

}
