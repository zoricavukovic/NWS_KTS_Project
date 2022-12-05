import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class PaymentService {

  constructor(
    private http: HttpClient, 
    private configService: ConfigService
    ) { }

    createPayment(num: number) {
      return this.http.post<any>(
        "http://localhost:8080/paypal/create-payment",
        num,
        { headers: this.configService.getHeader() }
      );
    }
}
