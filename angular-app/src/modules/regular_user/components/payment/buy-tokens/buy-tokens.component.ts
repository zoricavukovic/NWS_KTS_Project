import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import {TokenBank} from "../../../../shared/models/payment/token-bank";
import {PaymentService} from "../../../services/payment.service";


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
    this.tokenForm.get('tokenFormControl').addValidators(Validators.max(this.tokenBank.payingInfo.maxNumOfTokensPerTransaction))
  }

  createPayment(): void {
    if (!this.tokenForm.invalid && !this.checkNumGreaterThanAllowed()) {
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
    } else {
      this.toast.error(
        `Number of tokens must be in range 1-${this.tokenBank.payingInfo.maxNumOfTokensPerTransaction}`,
        'Payment creation failed!'
      );
    }
  }

  checkNumGreaterThanAllowed(): boolean {

    return +this.tokenForm.get('tokenFormControl').value > this.tokenBank.payingInfo.maxNumOfTokensPerTransaction;
  }

  calculateValue(): string {
    const numOfTokens = +this.tokenForm.get('tokenFormControl').value;
    if (numOfTokens > 0) {

      return numOfTokens * this.tokenBank.payingInfo.tokenPrice + ' €';
    }

    return '0 €';
  }

  ngOnDestroy(): void {
    if (this.paymentSubscription) {
      this.paymentSubscription.unsubscribe();
    }

    this.tokenForm.reset();
  }

}
