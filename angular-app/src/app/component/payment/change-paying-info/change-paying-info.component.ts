import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { PayingInfo } from 'src/app/model/payment/paying-info';
import { PayingInfoService } from 'src/app/service/paying-info.service';
import { PaymentService } from 'src/app/service/payment.service';
import { TokenBankService } from 'src/app/service/token-bank.service';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-change-paying-info',
  templateUrl: './change-paying-info.component.html',
  styleUrls: ['./change-paying-info.component.css']
})
export class ChangePayingInfoComponent implements OnInit, OnDestroy {

  constructor(
    private _payingInfoService: PayingInfoService,
    private _tokenBankService: TokenBankService
  ) { }

  payingInfo: PayingInfo;
  payingInfoSubscription: Subscription;
  tokenBankSubscription: Subscription;

  totalMoneySpent: number = 0;
  totalTokenAmountSpent: number = 0;
  totalTokensInApp: number = 0;

  ngOnInit(): void {
    this.payingInfoSubscription = this._payingInfoService.get(environment.payingInfoId).subscribe(
      res => {
        if (res) {
          this.payingInfo = res;
        }
      }
    )

    this.tokenBankSubscription = this._tokenBankService.getInAppSpending().subscribe(
      res => {
        if (res) {
          this.totalMoneySpent = res.totalMoneySpent;
          this.totalTokenAmountSpent = res.totalTokenAmountSpent;
          this.totalTokensInApp = res.totalTokensInApp;
        }
      }
    );
  }

  ngOnDestroy(): void {
    if (this.payingInfoSubscription) {
      this.payingInfoSubscription.unsubscribe();
    }

    if (this.tokenBankSubscription) {
      this.tokenBankSubscription.unsubscribe();
    }
  }

}
