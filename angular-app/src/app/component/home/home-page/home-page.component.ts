import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { RouteService } from '../../../service/route.service';
import { User } from '../../../model/user/user';
import { Subscription } from 'rxjs';
import { AuthService } from '../../../service/auth.service';

@Component({
  selector: 'app-home-page',
  templateUrl: './home-page.component.html',
  styleUrls: ['./home-page.component.css'],
})
export class HomePageComponent implements OnInit, OnDestroy {
  @Input() map: google.maps.Map;
  currentUser: User;
  isDriver: boolean;
  isRegular: boolean;

  authSubscription: Subscription;

  constructor(private routeService: RouteService, private authService: AuthService) {
    this.currentUser = null;
    this.isDriver = false;
    this.isRegular = false;
  }

  ngOnInit(): void {
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.currentUser = user;
        this.isDriver = this.authService.userIsDriver();
        this.isRegular = this.authService.userIsRegular();
      });
  }

  ngOnDestroy(): void {
    if (this.authSubscription){
      this.authSubscription.unsubscribe();
    }
  }
}
