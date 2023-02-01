import { Component, Inject, OnDestroy } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DriverUpdateApprovalService } from 'src/modules/admin/services/driver-update-approval/driver-update-approval.service';
import { Driver } from 'src/modules/shared/models/user/driver';
import { DriverUpdateApproval } from 'src/modules/shared/models/user/update-approval';

@Component({
  selector: 'app-approval-dialog',
  templateUrl: './approval-dialog.component.html',
  styleUrls: ['./approval-dialog.component.css']
})
export class ApprovalDialogComponent implements OnDestroy {

  driver: Driver;
  updateApprovalSubscription: Subscription;

  constructor(
    @Inject(MAT_DIALOG_DATA) public request: DriverUpdateApproval,
    private driverUpdateApprovalService: DriverUpdateApprovalService,
    private toast: ToastrService
  ) { }

  approveChanges() {
    this.updateApprovalSubscription = this.driverUpdateApprovalService.approve(this.request.id).subscribe(
      res => {
        console.log(res);
        this.toast.success("Request is approved!", "Success")
      },
      err => {
        this.toast.error(
          err.error,
          'Error happened!'
        )
      }
    );
  }

  rejectChanges() {
    this.updateApprovalSubscription = this.driverUpdateApprovalService.reject(this.request.id).subscribe(
      res => {
        console.log(res);
        this.toast.success("Request is rejected!", "Rejected")
      },
      err => {
        this.toast.error(
          err.error,
          'Error happened!'
        )
      }
    );
  }

  ngOnDestroy(): void {
    if (this.updateApprovalSubscription) {
      this.updateApprovalSubscription.unsubscribe();
    }
  }
}
