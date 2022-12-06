import { Token } from '@angular/compiler';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { TokenBank } from 'src/app/model/payment/token-bank';
import { PaymentService } from 'src/app/service/payment.service';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { isFormValid } from 'src/app/util/validation-function';


@Component({
  selector: 'app-buy-tokens',
  templateUrl: './buy-tokens.component.html',
  styleUrls: ['./buy-tokens.component.css']
})
export class BuyTokensComponent implements OnInit, OnDestroy {

  @Input() tokenBank: TokenBank;
  
  paymentSubscription: Subscription;

  constructor(
    private paymentService: PaymentService,
    private toast: ToastrService
    ) {
    this.tokenBank = null;
  }

  tokenForm = new FormGroup(
    {
      tokenFormControl: new FormControl('', [
        Validators.pattern('[1-9][0-9]*')
      ])
  });

  ngOnInit(): void {
  }

  createPayment(): void {
    if (isFormValid(this.tokenForm) && !this.checkNumGreaterThanAllowed()) {
      this.paymentSubscription = this.paymentService.createPayment(
        this.paymentService.createPaymentRequest(this.tokenBank.id, Number(this.tokenForm.get('tokenFormControl').value)))
        .subscribe(
          res => {
            window.location.href = res.redirectUrl;
          },
          error => {
            this.toast.error(
              error.error,
              'Payment creation failed!'
            );
          }
      );
    }
  }

  checkNumGreaterThanAllowed(): boolean {

    return Number(this.tokenForm.get('tokenFormControl').value) > this.tokenBank.payingInfo.maxNumOfTokensPerTransaction;
  }

  calculateValue(): string {
    let numOfTokens: number = Number(this.tokenForm.get('tokenFormControl').value);
    if (numOfTokens > 0) {

      return numOfTokens * this.tokenBank.payingInfo.tokenPrice + ' €';
    }

    return '0 €';
  }

  ngOnDestroy(): void {
    if (this.paymentSubscription) {
      this.paymentSubscription.unsubscribe();
    }
  }

}
