import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { TokenBank } from 'src/app/model/payment/token-bank';
import { PaymentService } from 'src/app/service/payment.service';

@Component({
  selector: 'app-financial-card',
  templateUrl: './financial-card.component.html',
  styleUrls: ['./financial-card.component.css']
})
export class FinancialCardComponent implements OnInit {

  tokenBank: TokenBank;
  tokenBankSubscription: Subscription;
  userId: string = "";

  constructor(
    private paymentService: PaymentService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('id');
    if (this.userId) {
      this.tokenBankSubscription = this.paymentService.getTokenBank(this.userId).subscribe(
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
}
