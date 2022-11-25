import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/service/auth.service';
import { ConfigService } from 'src/app/service/config.service';

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css'],
})
export class NavBarComponent implements OnInit {
  isAdmin = false;
  isRegularUser = false;
  logoutSubscription: Subscription;

  constructor(
    public authService: AuthService,
    private router: Router,
    public configService: ConfigService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.getCurrentUser?.isUserAdmin();
    console.log(this.isAdmin);
    this.isRegularUser = this.authService.getCurrentUser?.userIsRegular();
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
      .setOfflineStatus()
      .subscribe(response => {
        console.log(response);
      });
    this.authService.logOut();
    this.router.navigate(['/login']);
  }
}
