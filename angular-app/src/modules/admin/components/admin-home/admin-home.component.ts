import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { DriverUpdateApproval } from 'src/modules/shared/models/user/update-approval';
import { User } from 'src/modules/shared/models/user/user';
import { DriverUpdateApprovalService } from '../../services/driver-update-approval/driver-update-approval.service';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent implements OnInit, OnDestroy {

  @Input() currentUser: User;

  requests: DriverUpdateApproval[];
  updateApprovalSubscription: Subscription;

  constructor(
    private driverUpdateApprovalService: DriverUpdateApprovalService
  ) {
    this.requests = [];
  }

  ngOnInit(): void {
   this.loadRequests();
  }

  loadRequests(): void  {
    this.updateApprovalSubscription = this.driverUpdateApprovalService.getAll().subscribe(
      res => {
        if (res) {
          this.requests = res;
        }
      }
    );
  }

  changedRequest(): void {
    this.loadRequests();
  }

  ngOnDestroy(): void {
    if (this.updateApprovalSubscription) {
      this.updateApprovalSubscription.unsubscribe();
    }
  }

}
