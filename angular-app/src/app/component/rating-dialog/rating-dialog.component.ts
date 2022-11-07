import { Component, OnInit, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-rating-dialog',
  templateUrl: './rating-dialog.component.html',
  styleUrls: ['./rating-dialog.component.css']
})
export class RatingDialogComponent implements OnInit {

  val:number=4;
  ratingVehicle:number = 5;
   ratingDriver:number = 0;
   id:number;

  constructor( private dialogRef: MatDialogRef<RatingDialogComponent>,
    @Inject(MAT_DIALOG_DATA) data) {
      this.id = data.id;

   }
  cancelBtn(): void {
    this.dialogRef.close();
  }

  confirm(): void{
    this.dialogRef.close({"ratingVehicle":this.ratingVehicle, "ratingDriver": this.ratingDriver, "id": this.id});
  }

  ngOnInit(): void {
  }

}
