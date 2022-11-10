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
import { FavouriteRouteRequest } from 'src/app/model/request/favourite-route-request';
import { UserService } from 'src/app/service/user.service';

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
  favouriteRouteSubscription: Subscription;

  constructor( private route: ActivatedRoute, private http: HttpClient, private configService: ConfigService, private authService: AuthService, private userService: UserService) { }

  ngOnInit(): void {


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
        console.log(response);
       })

       this.currentUserSubscription = this.authService.getCurrentUser().subscribe(
        (response) => 
        {
          this.currentUser=response;
          this.favouriteRouteSubscription = this.userService.isFavouriteRouteForUser(this.driving.route.id, this.currentUser.email).subscribe(
            (response) => 
            {
              if(response) 
              {
                this.favouriteRoute = true;
              }
            }
          )
        });

   })

  }

  setFavouriteRoute(){
    if(this.favouriteRoute){
      this.userService.removeFromFavouriteRoutes(new FavouriteRouteRequest(this.currentUser.email, this.driving.route.id)).subscribe(
        res => {this.favouriteRoute = false; console.log(res);}
      );
    }
    else{
    
      this.userService.addToFavouriteRoutes(new FavouriteRouteRequest(this.currentUser.email, this.driving.route.id)).subscribe(
        res => {this.favouriteRoute = true; console.log(res);}
      );
    }
  }

  getBase64Prefix(): string {
    return this.configService.base64_show_photo_prefix
  }

  isRegularUser(): boolean{
    return this.currentUser.role.name === "ROLE_REGULAR_USER";
  }

  isDriver(): boolean{
    return this.currentUser.role.name === "ROLE_DRIVER";
  }

  ngOnDestroy(): void {
    if(this.currentUserSubscription){
      this.currentUserSubscription.unsubscribe();
    }
    
    if(this.drivingsSubscription){
      this.drivingsSubscription.unsubscribe();
    }

    if(this.driverSubscription){
      this.driverSubscription.unsubscribe();
    }

    if(this.favouriteRouteSubscription){
      this.favouriteRouteSubscription.unsubscribe();
    }
  }

}