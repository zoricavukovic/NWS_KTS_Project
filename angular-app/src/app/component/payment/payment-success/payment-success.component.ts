import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PaymentService } from 'src/app/service/payment.service';

@Component({
  selector: 'app-payment-success',
  templateUrl: './payment-success.component.html',
  styleUrls: ['./payment-success.component.css']
})
export class PaymentSuccessComponent implements OnInit {

  constructor(
    private router: Router
  ) { }

  ngOnInit(): void {
  }

  test() {
    console.log("usloo");
    
  } 



}
