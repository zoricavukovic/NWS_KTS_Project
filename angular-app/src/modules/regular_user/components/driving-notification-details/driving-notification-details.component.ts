import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { DrivingNotification } from 'src/modules/shared/models/notification/driving-notification';
import { User } from 'src/modules/shared/models/user/user';
import { ConfigService } from 'src/modules/shared/services/config-service/config.service';
import { DrivingNotificationService } from 'src/modules/shared/services/driving-notification-service/driving-notification.service';
import { RouteService } from 'src/modules/shared/services/route-service/route.service';
import { UserService } from 'src/modules/shared/services/user-service/user.service';
import { drawAllMarkers, drawPolylineWithLngLatArray } from 'src/modules/shared/utils/map-functions';
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {ToastrService} from "ngx-toastr";

@Component({
  selector: 'app-driving-notification-details',
  templateUrl: './driving-notification-details.component.html',
  styleUrls: ['./driving-notification-details.component.css']
})
export class DrivingNotificationDetailsComponent implements OnInit, OnDestroy {
  @Input() map: google.maps.Map;
  id:number;
  currentUser: User;
  base64Prefix = this.configService.BASE64_PHOTO_PREFIX;
  drivingNotification: DrivingNotification;
  routePolyline: google.maps.Polyline;
  markers: google.maps.Marker[];
  users: User[];
  answered: boolean;

  drivingNotificationSubscription: Subscription;
  routeSubscription: Subscription;
  userSubscription: Subscription;
  authSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private drivingNotificationService: DrivingNotificationService,
    private router: Router,
    private routeService: RouteService,
    private configService: ConfigService,
    private userService: UserService,
    private authService: AuthService,
    private toastService: ToastrService
    ) {
      this.users = [];
      this.answered = false;
     }

  ngOnInit(): void {
    // this.router.events.subscribe((event) => {
    //   this.ngOnDestroy();
    // });
    this.id = +this.route.snapshot.paramMap.get('id');
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.currentUser = user;
      });

    this.drivingNotificationSubscription = this.drivingNotificationService.get(this.id).subscribe(
      (response) => {
        console.log(this.map);
        this.drivingNotification = response;
        this.users = response.receivers;
        if (this.map){
          this.routeSubscription = this.routeService.getRoutePath(this.drivingNotification?.route?.id).subscribe(path => {
              this.routePolyline = drawPolylineWithLngLatArray(this.map, path);
              this.markers = drawAllMarkers(this.drivingNotification?.route?.locations, this.map);
            }
          )
        }
        this.userSubscription = this.userService.getUserByEmail(response.senderEmail).subscribe(response => {
          this.users.push(response);
        })
      }
    )
  }

  acceptRide() {
    this.answered = true;
    this.drivingNotificationService.updateRideStatus(this.id, true, this.currentUser.email).subscribe(res => {
      console.log(res);
    }, err => this.toastService.error(err.error, "Accepting ride failed"));
  }

  rejectRide() {
    this.answered = true;
    this.drivingNotificationService.updateRideStatus(this.id, false, this.currentUser.email).subscribe(res => {
      console.log(res);
    }, err => this.toastService.error(err.error, "Rejecting ride failed"));
  }

  ngOnDestroy(): void {
    if(this.drivingNotificationSubscription){
      this.drivingNotificationSubscription.unsubscribe();
    }

    if(this.routeSubscription){
      this.routeSubscription.unsubscribe();
    }

    if(this.userSubscription){
      this.userSubscription.unsubscribe();
    }

    if(this.authSubscription){
      this.authSubscription.unsubscribe();
    }
  }
}