import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import {ReportService} from "../../services/report-service/report.service";
import {Report} from "../../../shared/models/report/report";

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
    private reportService: ReportService
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

  ngOnDestroy(): void {
    if (this.reportSubscription) {
      this.reportSubscription.unsubscribe();
    }
  }
}
