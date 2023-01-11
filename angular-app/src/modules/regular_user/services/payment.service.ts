import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {ConfigService} from "../../shared/services/config-service/config.service";
import {CreatePayment, RedirectInfo} from "../../shared/models/payment/payment";

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(
    private http: HttpClient,
    private configService: ConfigService
    ) {
    }

    createPaymentRequest(tokenBankId: number, numOfTokens: number): CreatePayment {

      return {tokenBankId: tokenBankId, numOfTokens: numOfTokens};
    }

    completePaymentRequest(tokenBankId: number, numOfTokens: number, payerId: string, paymentId: string): CreatePayment {

      return {tokenBankId: tokenBankId, numOfTokens: numOfTokens, payerId: payerId, paymentId: paymentId};
    }

    createPayment(data: CreatePayment): Observable<RedirectInfo> {
      return this.http.post<RedirectInfo>(
        this.configService.CREATE_PAYMENT_URL,
        data
      );
    }

    completePayment(data: CreatePayment): Observable<boolean> {
      return this.http.post<boolean>(
        this.configService.COMPLETE_PAYMENT_URL,
        data
      );
    }
}
