import { Component, Input, OnInit } from '@angular/core';
import { TokenTransaction } from 'src/app/model/payment/token-transaction';

@Component({
  selector: 'app-transactions-history',
  templateUrl: './transactions-history.component.html',
  styleUrls: ['./transactions-history.component.css']
})
export class TransactionsHistoryComponent implements OnInit {

  @Input() transactions: TokenTransaction[]; 

  constructor() { }

  ngOnInit(): void {
  }

}
