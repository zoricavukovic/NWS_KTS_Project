import { Component, OnInit, Input, OnDestroy } from '@angular/core';
import { Driving } from 'src/app/model/response/driving';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { RatingDialogComponent } from '../../review/rating-dialog/rating-dialog.component';
import { ConfigService } from 'src/app/service/config.service';
import { ReviewService } from 'src/app/service/review.service';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/response/user/user';
import {ToastrService} from "ngx-toastr";

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

  constructor(private router: Router,
              private dialog: MatDialog,
              private reviewService: ReviewService,
              private configService: ConfigService,
              private toast: ToastrService
  ) { }

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

      res => {
        this.toast.success("Review created");
        this.driving.hasReviewForUser = true;
      },

      err => {
        this.toast.error("Review creation failed");
      }
      /*res => {this.toast.success({detail:"Review created", summary:"Review is successfully created!", 
                duration:4000, position:'bl'});
                this.driving.hasReviewForUser = true;
              },
      error => this.toast.error({detail:"Review creation failed", summary:error.error, 
                duration:4000, position:'bl'})*/
      );
  }

  



  ngOnDestroy(): void {

    if(this.reviewSubscription){
      this.reviewSubscription.unsubscribe();
    }

   }

}
