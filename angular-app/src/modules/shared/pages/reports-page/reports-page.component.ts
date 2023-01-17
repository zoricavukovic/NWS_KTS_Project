import { Component } from '@angular/core';
import { MatTabChangeEvent } from '@angular/material/tabs';
import { ConfigService } from '../../services/config-service/config.service';

@Component({
  selector: 'app-reports-page',
  templateUrl: './reports-page.component.html',
  styleUrls: ['./reports-page.component.css']
})
export class ReportsPageComponent {

  selectedReport: string;
  selectedTabIndex: number;

  constructor(
    private configService: ConfigService
  ) {
    this.selectedReport = configService.SELECTED_SPENDING_REPORT;
    this.selectedTabIndex = 0;
  }

  tabChanged(tabChangeEvent: MatTabChangeEvent): void {
    if (tabChangeEvent.index === 0) {
      this.selectedReport = this.configService.SELECTED_SPENDING_REPORT;
      this.selectedTabIndex = 0;
    } else if (tabChangeEvent.index === 1) {
      this.selectedReport = this.configService.SELECTED_RIDES_REPORT;
      this.selectedTabIndex = 1;
    } else if (tabChangeEvent.index === 2) {
      this.selectedReport = this.configService.SELECTED_DISTANCE_REPORT;
      this.selectedTabIndex = 2;
    }
  }

}
