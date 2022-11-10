import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { ConfigService } from 'src/app/service/config.service';
import { AuthService } from 'src/app/service/auth.service';
import { Driving } from 'src/app/model/response/driving';
import { User } from 'src/app/model/response/user/user';
import { PageEvent } from '@angular/material/paginator';

@Component({
  selector: 'app-show-drivings',
  templateUrl: './show-drivings.component.html',
  styleUrls: ['./show-drivings.component.css']
})
export class ShowDrivingsComponent implements OnInit, OnDestroy {
  currentUserSubscription: Subscription;
  reviewSubscription: Subscription;

  constructor( private http: HttpClient, private authService: AuthService,private configService: ConfigService) { }

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
  
  ngOnInit(): void {
    this.currentUserSubscription = this.authService.getCurrentUser().subscribe((data) => this.currentUser=data);
    this.http.get(this.configService.drivings_url(this.currentUser.email, this.pageNumber, this.pageSize,this.selectedSortBy,this.selectedSortOrder)).subscribe((response:any) => {
        this.drivings = response;
        this.http.get(this.configService.reviewed_drivings_url + this.currentUser.email).subscribe((response: any) => {
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
    this.http.get(this.configService.drivings_url(this.currentUser.email, this.pageNumber, this.pageSize,this.selectedSortBy,this.selectedSortOrder)).subscribe((response:any) => {
      this.drivings = response;
    });
  }

  selectSort(name: string){
    this.selectedSortBy = name;
    this.http.get(this.configService.drivings_url(this.currentUser.email, this.pageNumber, this.pageSize,this.selectedSortBy,this.selectedSortOrder)).subscribe((response:any) => {
      this.drivings = response;
    });
  }

  onPaginate(pageEvent: PageEvent) {
    this.pageSize = +pageEvent.pageSize;
    this.pageNumber = +pageEvent.pageIndex;
    console.log(this.pageNumber, this.pageSize);
    this.http.get(this.configService.drivings_url(this.currentUser.email, this.pageNumber, this.pageSize,this.selectedSortBy,this.selectedSortOrder)).subscribe((response:any) => {
      this.drivings = response;
    });
    }


  ngOnDestroy(): void {
    if(this.currentUserSubscription){
      this.currentUserSubscription.unsubscribe();
    }
  }
   }


