import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
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
  
  showLoadingScreen: boolean = true;

  paymentSubscription: Subscription;
  pickedNumOfTokensSubscription: Subscription; 

  constructor(
    private activatedRoute: ActivatedRoute,
    private paymentService: PaymentService,
    private toast: ToastrService,
    private route: Router
  ) {}

  ngOnInit(): void {
    this.numOfTokens = +this.activatedRoute.snapshot.paramMap.get('numOfTokens');
    this.tokenBankId = +this.activatedRoute.snapshot.paramMap.get('tokenBankId');

    this.activatedRoute.queryParams.subscribe(params => {
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
          this.showLoadingScreen = false;
          this.toast.success(
            'Transaction is successfully completed.',
            'Payment completed!!'
            );
          this.route.navigate(['payment/status/1']);
        },
        error => {
          this.showLoadingScreen = false;
          this.toast.error(
            error.error,
            'Payment completion failed!'
            );
          this.route.navigate(['payment/status/-1']);
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
