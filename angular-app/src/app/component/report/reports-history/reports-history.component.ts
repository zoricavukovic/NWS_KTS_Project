import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Report } from 'src/app/model/report/report';
import { ConfigService } from 'src/app/service/config.service';
import { ReportService } from 'src/app/service/report.service';

@Component({
  selector: 'app-reports-history',
  templateUrl: './reports-history.component.html',
  styleUrls: ['./reports-history.component.css']
})
export class ReportsHistoryComponent implements OnInit, OnDestroy {

  @Input() userId: number;
  
  reportSubscription: Subscription;
  reports: Report[];

  constructor(
    private reportService: ReportService,
    private configService: ConfigService
  ) { }

  ngOnInit(): void {
    if (this.userId) {
      this.loadDriverReviews();
    }
  }

  loadDriverReviews(): void {
    this.reportSubscription = this.reportService.getReports(this.userId).subscribe(
      reports => {
        if (reports !== null && reports !== undefined) {
          this.reports = reports;
        }
      }
    );
  }

  getBase64Prefix(): string {

    return this.configService.base64_show_photo_prefix;
  }

  ngOnDestroy(): void {
    if (this.reportSubscription) {
      this.reportSubscription.unsubscribe();
    }
  }

}
