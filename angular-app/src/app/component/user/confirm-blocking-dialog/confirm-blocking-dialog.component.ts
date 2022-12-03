import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from 'src/app/model/user/user';
import { BlockNotification } from 'src/app/model/notification/block-notification';

@Component({
  selector: 'app-confirm-blocking-dialog',
  templateUrl: './confirm-blocking-dialog.component.html',
  styleUrls: ['./confirm-blocking-dialog.component.css']
})
export class ConfirmBlockingDialogComponent implements OnInit {

  confirmed: boolean = false;
  reason: string = "";

  constructor(@Inject(MAT_DIALOG_DATA) public user: User) { }

  ngOnInit(): void {
  }

  checkEnteredReason(): boolean {

    return this.reason.length > 4;
  }

  createData(): BlockNotification {
    
    return {userId: this.user.id, reason: this.reason} 
  }

}
