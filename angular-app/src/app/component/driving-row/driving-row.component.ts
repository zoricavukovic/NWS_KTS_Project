import { Component, OnInit, Input, OnDestroy, Output, EventEmitter } from '@angular/core';
import { Driving } from 'src/app/model/driving';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RatingDialogComponent } from '../rating-dialog/rating-dialog.component';
import { ConfigService } from 'src/app/service/config.service';
import { ReviewService } from 'src/app/service/review.service';
import { ReviewRequest } from 'src/app/model/review-request';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/user';

@Component({
  selector: 'driving-row',
  templateUrl: './driving-row.component.html',
  styleUrls: ['./driving-row.component.css']
})
export class DrivingRowComponent implements OnInit, OnDestroy {

  @Input() driving: Driving; 
  @Input() index: number;
  @Input() user: User;

  dataRate;

  reviewSubscription: Subscription;

  constructor(public router: Router,  public dialog: MatDialog, public reviewService: ReviewService, public configService: ConfigService) { }

  ngOnInit(): void {
  }

  goToDetailsPage(id: number){

    this.router.navigate(["/details", id]);
  }

  isDisabledBtnRate(date, id: number) : boolean{
    const date_today:Date = new Date();

    return !(this.getDifferenceInDays(date, date_today) > 3);
   }

  getDifferenceInDays(date1, date2):number {
    const diffInMs = Math.abs(date2 - Date.parse(date1));

    return diffInMs / (1000 * 60 * 60 * 24);
  }

  endDrivingDate(startDate, duration){
    let start = new Date(Date.parse(startDate));

    return new Date(start.getTime() + duration*60000)
   }

  haveDrivingRate(id: number){
    if(this.configService.have_driving_rate_url + id){
      return true;
    }
    return false;
 }

  openDialog(id:number){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.data ={
      id: id,
      userEmail: this.user.email
    }
    const dialogRef = this.dialog.open(RatingDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      result => {
        console.log(result);
        this.driving.hasReviewForUser = true;
      });
  }


  ngOnDestroy(): void {
   
    if(this.reviewSubscription){
      this.reviewSubscription.unsubscribe();
    }

   }

}
