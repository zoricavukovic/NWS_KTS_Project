import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import {User} from "../../models/user/user";
import {BlockNotification} from "../../models/notification/block-notification";

@Component({
  selector: 'app-confirm-blocking-dialog',
  templateUrl: './confirm-blocking-dialog.component.html',
  styleUrls: ['./confirm-blocking-dialog.component.css']
})
export class ConfirmBlockingDialogComponent {

  reason: string;

  constructor(@Inject(MAT_DIALOG_DATA) public user: User) {
    this.reason = "";
  }

  checkEnteredReason(): boolean {

    return this.reason.length > 4;
  }

  createData(): BlockNotification {

    return {userId: this.user.id, reason: this.reason}
  }

}
