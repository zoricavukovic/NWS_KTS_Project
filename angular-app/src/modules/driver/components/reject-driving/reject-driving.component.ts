import {Component, Inject } from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from "@angular/material/dialog";

export interface DialogData {
  reasonForRejectingDriving: string;
}

@Component({
  selector: 'app-reject-driving',
  templateUrl: './reject-driving.component.html',
  styleUrls: ['./reject-driving.component.css']
})
export class RejectDrivingComponent {

  customReason = '';
  defaultReason = '';
  confirmIsPress = false;

  constructor(
    public dialogRef: MatDialogRef<RejectDrivingComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {}

  cancel(): void {
    this.dialogRef.close();
  }

  confirm(): void {
    this.confirmIsPress = true;
    if (this.isValidForm() && this.confirmIsPress){
      this.data.reasonForRejectingDriving = this.defaultReason !=='' ? this.defaultReason : this.customReason;
      this.dialogRef.close(this.data.reasonForRejectingDriving);
    }
  }

  isValidForm() {
    return this.defaultReason !== '' || this.customReason !== '';
  }
}
