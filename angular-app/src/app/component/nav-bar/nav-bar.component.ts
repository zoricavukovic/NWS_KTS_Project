import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit, OnDestroy {

  currentUser: User;
  currentUserSubscription: Subscription;
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.currentUserSubscription = this.authService.getCurrentUser().subscribe((data) => this.currentUser=data);
  }

  logOut(){

    this.authService.logOut();
    this.router.navigate(['/home-page']);
  }

  ngOnDestroy(): void {
    this.currentUserSubscription.unsubscribe();
  }
}
