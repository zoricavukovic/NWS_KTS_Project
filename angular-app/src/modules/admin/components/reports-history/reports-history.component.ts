import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import {ReportService} from "../../services/report-service/report.service";
import {ConfigService} from "../../../shared/services/config-service/config.service";
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
    private reportService: ReportService,
    private configService: ConfigService,
    private router: Router
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

  goToUserProfile(id: number): void {
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>
    this.router.navigate([`/serb-uber/user/user-profile/${id}`]));
  }

  getBase64Prefix(): string {

    return this.configService.BASE64_PHOTO_PREFIX;
  }

  ngOnDestroy(): void {
    if (this.reportSubscription) {
      this.reportSubscription.unsubscribe();
    }
  }

}
