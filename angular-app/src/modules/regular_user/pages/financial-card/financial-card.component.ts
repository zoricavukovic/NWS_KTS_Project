import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import {TokenBank} from "../../../shared/models/payment/token-bank";
import {TokenBankService} from "../../../shared/services/token-bank-service/token-bank.service";

@Component({
  selector: 'app-financial-card',
  templateUrl: './financial-card.component.html',
  styleUrls: ['./financial-card.component.css']
})
export class FinancialCardComponent implements OnInit, OnDestroy {

  tokenBank: TokenBank;
  tokenBankSubscription: Subscription;
  userId: string;

  constructor(
    private _tokenBankService: TokenBankService,
    private route: ActivatedRoute
  ) {
    this.userId = "";
  }

  ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('id');
    if (this.userId) {
      this.tokenBankSubscription = this._tokenBankService.get(+this.userId).subscribe(
        response => {
          if (response) {
            this.tokenBank = response;
            console.log(this.tokenBank);
          }
        },
        error => {
          console.log(error)
        }
      );
    }
  }

  ngOnDestroy(): void {
    if (this.tokenBankSubscription) {
      this.tokenBankSubscription.unsubscribe();
    }
  }
}
