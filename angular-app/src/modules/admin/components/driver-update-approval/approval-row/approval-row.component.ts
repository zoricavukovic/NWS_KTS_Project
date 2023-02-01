import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DriverUpdateApproval } from 'src/modules/shared/models/user/update-approval';
import { ApprovalDialogComponent } from '../approval-dialog/approval-dialog.component';

@Component({
  selector: 'app-approval-row',
  templateUrl: './approval-row.component.html',
  styleUrls: ['./approval-row.component.css']
})
export class ApprovalRowComponent {

  @Input() request: DriverUpdateApproval;
  @Output() changedRequestEvent = new EventEmitter();

  constructor(
    private dialog: MatDialog
  ) {
    this.request = null;
  }

  showRequest() {
    const dialogRef = this.dialog.open(ApprovalDialogComponent, {
      data: this.request,
    });

    dialogRef.afterClosed().subscribe(res => {
      if (res) {
        this.changedRequestEvent.emit();
      }
    });
  }
}
