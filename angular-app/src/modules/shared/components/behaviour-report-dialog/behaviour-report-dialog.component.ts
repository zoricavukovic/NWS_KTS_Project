import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { ReportService } from 'src/modules/admin/services/report-service/report.service';
import { BehaviourReportDialogData } from '../../models/report/report';

@Component({
  selector: 'app-behaviour-report-dialog',
  templateUrl: './behaviour-report-dialog.component.html',
  styleUrls: ['./behaviour-report-dialog.component.css']
})
export class BehaviourReportDialogComponent implements OnDestroy {

  reason: string;
  reportSubscription: Subscription;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: BehaviourReportDialogData,
    private reportService: ReportService,
    private toast: ToastrService
  ) {
    this.reason = '';
  }

  checkEnteredReason() {

    return this.reason.length > 5;
  }

  createReport(): void {
    let senderId: number;
    let receiverId: number;

    if (this.data.userToReport) {
      senderId = this.data.currentUser.id;
      receiverId = this.data.userToReport.id;
    } else {
      senderId = this.data.currentUser.id;
      receiverId = this.data.driver.id;
    }

    this.reportSubscription = this.reportService.createReport(senderId, receiverId, this.reason).subscribe(
      res => {
        this.toast.success("Thank you for your feedback.", 'Your report is created!')
      },
      error => {
        this.toast.error(error.error, 'Report creation failed!')
      }
    );

  }

  ngOnDestroy(): void {
    if (this.reportSubscription) {
      this.reportSubscription.unsubscribe();
    }
  }

}
