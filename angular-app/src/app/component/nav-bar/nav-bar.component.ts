import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/response/user/user';
import { AuthService } from 'src/app/service/auth.service';
import { ConfigService } from 'src/app/service/config.service';

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css'],
})
export class NavBarComponent implements OnInit, OnDestroy {
  currentUser: User;
  isAdmin = false;
  isRegularUser = false;
  currentUserSubscription: Subscription;
  logoutSubscription: Subscription;

  constructor(
    private authService: AuthService,
    private router: Router,
    public configService: ConfigService
  ) {}

  ngOnInit(): void {
    this.currentUserSubscription = this.authService
      .getCurrentUser()
      .subscribe((user: User) => {
        this.currentUser = user;
        if (user!== null) {
          this.isAdmin = user.isUserAdmin();
          this.isRegularUser = user.userIsRegular();
        }
      });
  }

  redirectToEditPage() {
    this.router.navigate(['/edit-profile-data']);
  }

  redirectToProfilePage() {
    this.router.navigate(['/profile-page']);
  }

  redirectToMessagesPage() {
    this.router.navigate(['/messages']);
  }

  logOut() {
    this.logoutSubscription = this.authService
      .setOfflineStatus(this.currentUser)
      .subscribe(response => {
        console.log(response);
        console.log('Logut');
      });
    this.authService.logOut();
    this.router.navigate(['/home-page']);
  }

  ngOnDestroy(): void {
    this.currentUserSubscription.unsubscribe();
  }
}
