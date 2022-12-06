import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { CreatePayment } from 'src/app/model/payment/payment';
import { PaymentService } from 'src/app/service/payment.service';

@Component({
  selector: 'app-processing-payment',
  templateUrl: './processing-payment.component.html',
  styleUrls: ['./processing-payment.component.css']
})
export class ProcessingPaymentComponent implements OnInit, OnDestroy {

  payerId: string = '';
  paymentId: string = '';
  tokenBankId: number;
  numOfTokens: number;

  paymentSubscription: Subscription;
  pickedNumOfTokensSubscription: Subscription; 

  constructor(
    private route: ActivatedRoute,
    private paymentService: PaymentService
  ) {}

  ngOnInit(): void {
    this.numOfTokens = +this.route.snapshot.paramMap.get('numOfTokens');
    this.tokenBankId = +this.route.snapshot.paramMap.get('tokenBankId');

    this.route.queryParams.subscribe(params => {
      this.payerId = params['PayerID'];
      this.paymentId = params['paymentId'];
    });

    if (this.checkValidityOfParams()) {
      this.completePayment();
    }
  }

  completePayment(): void {
    this.paymentSubscription = this.paymentService.completePayment(
      this.paymentService.completePaymentRequest(this.tokenBankId, this.numOfTokens, this.payerId, this.paymentId))
      .subscribe(
        response => {
          console.log(response);
        },
        error => {
          console.log(error);
        }
      );
  }

  checkValidityOfParams(): boolean {
    
    return this.checkPayPalArguments() && this.checkTokenBankArguments();
  }

  checkPayPalArguments(): boolean {

    return (this.payerId !== '' && this.payerId && this.paymentId !== '' && this.paymentId) ? true : false;
  }

  checkTokenBankArguments(): boolean {

    return (this.numOfTokens && this.tokenBankId) ? true : false;
  }

  ngOnDestroy(): void {
    if (this.paymentSubscription) {
      this.paymentSubscription.unsubscribe();
    }

    if (this.pickedNumOfTokensSubscription) {
      this.pickedNumOfTokensSubscription.unsubscribe();
    }

  }

}
