import { Component, Input } from '@angular/core';
import {TokenTransaction} from "../../../../shared/models/payment/token-transaction";

@Component({
  selector: 'app-transactions-history',
  templateUrl: './transactions-history.component.html',
  styleUrls: ['./transactions-history.component.css']
})
export class TransactionsHistoryComponent {

  @Input() transactions: TokenTransaction[];

}
