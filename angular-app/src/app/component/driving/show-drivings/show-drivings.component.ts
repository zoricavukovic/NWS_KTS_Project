import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { ConfigService } from 'src/app/service/config.service';
import { AuthService } from 'src/app/service/auth.service';
import { Driving } from 'src/app/model/response/driving';
import { User } from 'src/app/model/response/user/user';
import { PageEvent } from '@angular/material/paginator';
import { DrivingService } from 'src/app/service/driving.service';
import { ReviewService } from 'src/app/service/review.service';

@Component({
  selector: 'app-show-drivings',
  templateUrl: './show-drivings.component.html',
  styleUrls: ['./show-drivings.component.css']
})
export class ShowDrivingsComponent implements OnInit, OnDestroy {
  drivings: Driving[] = [];
  currentUser: User;
  pageSize: number = 1;
  pageNumber: number = 0;
  selectedSortBy: string = "Date";
  selectedSortOrder: string = "Descending";

  sortOrder = [
    {name:"Descending", checked: true}, 
    {name:"Ascending", checked: false}
  ]

  sortBy = [
    {name:"Date", checked: true},
    {name:"Departure", checked: false},
    {name:"Destination", checked: false},
    {name:"Price", checked: false}
  ]

  currentUserSubscription: Subscription;
  drivingsSubscription: Subscription;
  reviewSubscription: Subscription;
  reviewedDrivingsSubscription: Subscription;

  constructor( private authService: AuthService, private drivingService: DrivingService, private reviewService: ReviewService) { }
  
  ngOnInit(): void {
    this.currentUserSubscription = this.authService.getCurrentUser().subscribe(
      (user) => {
        this.currentUser=user;
        if(!user.isUserAdmin()){
          this.drivingService.setUserEmail = this.currentUser.email;
        }
      });
    
    this.drivingsSubscription = this.drivingService.getDrivingsForUser(this.pageNumber, this.pageSize, this.selectedSortBy, this.selectedSortOrder).subscribe((response:any) => {
        this.drivings = response;
        this.reviewedDrivingsSubscription= this.reviewService.getReviewedDrivingsForUser(this.currentUser.email).subscribe((response: any) => {
          for(let driving of this.drivings){
            if(response.includes(driving.id)){
              driving.hasReviewForUser = true;
            }
          }
        })
    })
   }

  selectSortOrder(name: string){
    this.selectedSortOrder = name;
    this.drivingService.getDrivingsForUser(this.pageNumber, this.pageSize, this.selectedSortBy, this.selectedSortOrder).subscribe((response:any) => {
      this.drivings = response;
    });
  }

  selectSort(name: string){
    this.selectedSortBy = name;
    this.drivingService.getDrivingsForUser(this.pageNumber, this.pageSize, this.selectedSortBy, this.selectedSortOrder).subscribe((response:any) => {
      this.drivings = response;
    });
  }

  onPaginate(pageEvent: PageEvent) {
    this.pageSize = +pageEvent.pageSize;
    this.pageNumber = +pageEvent.pageIndex;
    this.drivingService.getDrivingsForUser(this.pageNumber, this.pageSize, this.selectedSortBy, this.selectedSortOrder).subscribe((response:any) => {
      this.drivings = response;
    });
    }


  ngOnDestroy(): void {
    if(this.currentUserSubscription){
      this.currentUserSubscription.unsubscribe();
    }
    if(this.reviewSubscription){
      this.reviewSubscription.unsubscribe();
    }
    if(this.drivingsSubscription){
      this.drivingsSubscription.unsubscribe();
    }
    if(this.reviewedDrivingsSubscription){
      this.reviewedDrivingsSubscription.unsubscribe();
    }
  }
}


