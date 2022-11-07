import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { ConfigService } from 'src/app/service/config.service';
import { Router } from '@angular/router'; 
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import { MatDialogConfig } from '@angular/material/dialog';
import { RatingDialogComponent } from '../rating-dialog/rating-dialog.component';
import { AuthService } from 'src/app/service/auth.service';
import { Driving } from 'src/app/model/driving';
import { User } from 'src/app/model/user';
import { ReviewRequest } from 'src/app/model/review-request';
import { ReviewService } from 'src/app/service/review.service';

@Component({
  selector: 'app-show-drivings',
  templateUrl: './show-drivings.component.html',
  styleUrls: ['./show-drivings.component.css']
})
export class ShowDrivingsComponent implements OnInit, OnDestroy {
  currentUserSubscription: Subscription;
  reviewSubscription: Subscription;

  constructor( private http: HttpClient, private authService: AuthService,private configService: ConfigService, private router: Router, public dialog: MatDialog, private reviewService: ReviewService) { }

  drivings: Driving[] = [];
  currentUser: User;
  
  ngOnInit(): void {
    this.currentUserSubscription = this.authService.getCurrentUser().subscribe((data) => this.currentUser=data);
    this.http.get(this.configService.drivings_url + this.currentUser.email).subscribe((response:any) => {
        this.drivings = response;
        console.log(response[response.length-1])
    })
   }

   sortByPrice(){
    const sorted = this.drivings.sort(
      (objA, objB) => objA.price - objB.price,
    );
    console.log(sorted);
   }

   /*sortByDate(){
    const sorted = this.drivings.sort(
      (objA:Driving, objB:Driving) => {Number(new Date(Date.parse(objA.started))) - Number(new Date(Date.parse(objB.started)))},
    );
    console.log(sorted);
   }*/

   endDrivingDate(startDate, duration){
    let start = new Date(Date.parse(startDate));
    console.log(start);
    return new Date(start.getTime() + duration*60000)

   }


   goToDetailsPage(id: number){
    console.log(id);
    this.router.navigate(["/details", id]);
   }

   isDisabledBtnRate(date, id: number) : boolean{
    const date_today:Date = new Date();
    if(this.getDifferenceInDays(date, date_today) > 3){
      return true;
    }
   /* else if(this.reviewService.haveReview(id) == true){
      console.log("pera");
      return true
    }*/
    else{
      return false;
    }
   }

   haveDrivingRate(id: number){
      if(this.configService.have_driving_rate_url + id){
        return true;
      }
      return false;
   }

   getDifferenceInDays(date1, date2):number {
    const diffInMs = Math.abs(date2 - Date.parse(date1));
    return diffInMs / (1000 * 60 * 60 * 24);
  }

   openDialog(id:number){
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.data ={
      id: id
    }


    const dialogRef = this.dialog.open(RatingDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      data => 
        this.reviewSubscription = this.reviewService.saveReview( new ReviewRequest(data.ratingVehicle, data.ratingDriver, "", data.id)).subscribe());
    } 


  ngOnDestroy(): void {
    this.currentUserSubscription.unsubscribe();
    this.reviewSubscription.unsubscribe();
  }
   }


