import { HttpClient } from '@angular/common/http';
import { Driving } from 'src/app/model/response/driving';
import { Component, OnDestroy, OnInit} from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ConfigService } from 'src/app/service/config.service';
import { AuthService } from 'src/app/service/auth.service';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/response/user/user';
import { Driver } from 'src/app/model/response/user/driver';
import {TooltipPosition} from '@angular/material/tooltip';

@Component({
  selector: 'app-driving-details',
  templateUrl: './driving-details.component.html',
  styleUrls: ['./driving-details.component.css','./driving-details.component.scss'],
})
export class DrivingDetailsComponent implements OnInit, OnDestroy {

  id:string;
  vehicleRating: number;
  driving:Driving = new Driving();
  driver: Driver = new Driver();
  currentUser: User;
  destinations: string[] = [];
  startPoint: string;
  favouriteRoute: boolean = false;
  positionOption: TooltipPosition = 'above';


  vehicle_image = {
    "VAN": '/assets/images/van.png',
    "SUV": '/assets/images/suv.png',
    "CAR": '/assets/images/car.png'
  };

  currentUserSubscription: Subscription;
  drivingsSubscription: Subscription;
  driverSubscription: Subscription;
  vehicleRatingSubscription: Subscription;

  constructor( private route: ActivatedRoute, private http: HttpClient, private configService: ConfigService, private authService: AuthService) { }

  ngOnInit(): void {


    this.currentUserSubscription = this.authService.getCurrentUser().subscribe((data) => this.currentUser=data);

    this.id = this.route.snapshot.paramMap.get('id');
    this.drivingsSubscription = this.http.get(this.configService.driving_details_url + this.id).subscribe((response: Driving) => {
      this.driving = response;
      console.log(this.driving);
      this.startPoint = this.driving.route.startPoint.street + " " + this.driving.route.startPoint.number;
      this.destinations.push(this.startPoint);
      for (let destination of this.driving.route.destinations) {
        this.destinations.push(destination.street + " " + destination.number);
      }

      this.driverSubscription = this.http.get(this.configService.driver_info_url + this.driving.driverEmail).subscribe((response: Driver) => {
        this.driver = response;
        console.log(this.driver);
  
       })

   })

  }

  setFavouriteRoute(){
    if(this.favouriteRoute){
      this.favouriteRoute = false;
      //unfavourite
    }
    else{
      this.favouriteRoute = true;
    }
  }

  ngOnDestroy(): void {
    this.currentUserSubscription.unsubscribe();
    this.drivingsSubscription.unsubscribe();
    this.driverSubscription.unsubscribe();
  }

}
