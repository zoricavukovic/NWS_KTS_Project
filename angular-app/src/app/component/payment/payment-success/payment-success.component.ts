import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { PaymentService } from 'src/app/service/payment.service';

@Component({
  selector: 'app-payment-success',
  templateUrl: './payment-success.component.html',
  styleUrls: ['./payment-success.component.css']
})
export class PaymentSuccessComponent implements OnInit, OnDestroy {

  constructor(
    private paymentService: PaymentService,
    private router: Router
  ) { }

  paymentSubscription: Subscription;

  ngOnInit(): void {
  }

  test() {
    console.log("usloo");
    this.paymentSubscription = this.paymentService.createPayment(3).subscribe(
      res => {
        console.log(res);
        window.location.href = res.redirectUrl;
      },
      error => {
        console.log(error);
      }
    );
  } 

  ngOnDestroy(): void {
    if (this.paymentSubscription) {
      this.paymentSubscription.unsubscribe();
    }
  }

}
