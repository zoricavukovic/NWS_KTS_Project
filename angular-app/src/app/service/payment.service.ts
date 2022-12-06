import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { CreatePayment, RedirectInfo } from '../model/payment/payment';
import { TokenBank } from '../model/payment/token-bank';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(
    private http: HttpClient, 
    private configService: ConfigService
    ) {}

    getTokenBank(userId: string): Observable<TokenBank> {
      return this.http.get<TokenBank>(this.configService.token_bank_by_user_id_url(userId), {
        headers: this.configService.getHeader()
      });
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
        data,
        { headers: this.configService.getHeader() }
      );
    }

    completePayment(data: CreatePayment): Observable<boolean> {
      return this.http.post<boolean>(
        this.configService.complete_payment_url,
        data,
        { headers: this.configService.getHeader() }
      );
    }
}
