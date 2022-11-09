import { Component, OnDestroy, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { ConfigService } from 'src/app/service/config.service';
import { AuthService } from 'src/app/service/auth.service';
import { Driving } from 'src/app/model/driving';
import { User } from 'src/app/model/user';

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
  
  ngOnInit(): void {
    this.currentUserSubscription = this.authService.getCurrentUser().subscribe((data) => this.currentUser=data);
    this.http.get(this.configService.drivings_url + this.currentUser.email).subscribe((response:any) => {
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

   sortByPrice(){
    const sorted = this.drivings.sort(
      (objA, objB) => objA.price - objB.price,
    );
    console.log(sorted);
   }


  ngOnDestroy(): void {
    if(this.currentUserSubscription){
      this.currentUserSubscription.unsubscribe();
    }
  }
   }

