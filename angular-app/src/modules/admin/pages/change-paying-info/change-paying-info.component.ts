import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { environment } from 'src/environments/environment';
import {PayingInfo} from "../../../shared/models/payment/paying-info";
import {PayingInfoService} from "../../services/paying-info-service/paying-info.service";
import {TokenBankService} from "../../../shared/services/token-bank-service/token-bank.service";

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

  totalMoneySpent = 0;
  totalTokenAmountSpent = 0;
  totalTokensInApp = 0;

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
