import { Component, Input } from '@angular/core';
import { User } from 'src/modules/shared/models/user/user';

@Component({
  selector: 'app-review-and-report-tabs',
  templateUrl: './review-and-report-tabs.component.html',
  styleUrls: ['./review-and-report-tabs.component.css']
})
export class ReviewAndReportTabsComponent {

  @Input() user: User;

  @Input() isDriver: boolean;

  @Input() loggedAdmin: boolean;

  constructor() {
    this.isDriver = false;
    this.loggedAdmin = false;
  }

}
