import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-payment-statistics',
  templateUrl: './payment-statistics.component.html',
  styleUrls: ['./payment-statistics.component.css']
})
export class PaymentStatisticsComponent implements OnInit {

  @Input() totalAmountMoneySpent: number;

  @Input() totalAmountTokenSpent: number;

  @Input() totalAmountTokenBought: number;

  constructor() { }

  ngOnInit(): void {
  }

  getTokenSpendingRation(): string {

    return (this.totalAmountTokenBought > 0)
        ? Math.round((this.totalAmountTokenSpent * 100 / this.totalAmountTokenBought)) + '%'
        : '0';
  }

  getTotalAmountMoney(): string {
    
    return this.totalAmountMoneySpent + '€';
  }

  getTotalAmountTokensBought(): string {

    return this.totalAmountTokenBought + '';
  }

  getMaxValueMoneySpent(): number {
    
    return (this.totalAmountMoneySpent === 0) ? 1 : this.totalAmountMoneySpent;
  }

  getMaxValueTokenBought(): number {

    return (this.totalAmountTokenBought === 0) ? 1 : this.totalAmountTokenBought;
  }

}
