import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { CreatePayment, RedirectInfo } from '../model/payment/payment';
import { InAppSpending, TokenBank } from '../model/payment/token-bank';
import { ConfigService } from './config.service';
import { GenericService } from './generic.service';

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
        this.configService.create_payment_url,
        data
      );
    }

    completePayment(data: CreatePayment): Observable<boolean> {
      return this.http.post<boolean>(
        this.configService.complete_payment_url,
        data
      );
    }
}
