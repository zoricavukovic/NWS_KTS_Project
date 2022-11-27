import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Driver } from 'src/app/model/user/driver';
import { AuthService } from 'src/app/service/auth.service';
import { ConfigService } from 'src/app/service/config.service';
import { DriverService } from 'src/app/service/driver.service';

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css'],
})
export class NavBarComponent implements OnInit {
  isAdmin = false;
  isRegularUser = false;
  logoutSubscription: Subscription;
  driverData: Driver;
  driverUpdateSubscription: Subscription;

  constructor(
    public authService: AuthService,
    private router: Router,
    private driverService: DriverService,
    public configService: ConfigService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.getCurrentUser?.isUserAdmin();
    console.log(this.isAdmin);
    this.isRegularUser = this.authService.getCurrentUser?.userIsRegular();
    
    if (this.authService.getCurrentUser?.userIsDriver()) {
      this.driverService.getDriver(this.authService.getCurrentUser.id)
      .subscribe((response: Driver) => {
        this.driverData = response;
      });
    }
  }

  changeDriverStatus() {
    this.driverUpdateSubscription = this.driverService.updateActivityStatus(
      this.driverService.createDriverUpdateActivityRequest(this.driverData.id, this.driverData.active)
    ).subscribe((response: Driver) => {
      this.driverData = response;
      console.log("Promenilo" + this.driverData.active)
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
      .setOfflineStatus()
      .subscribe(response => {
        console.log(response);
      });
    this.authService.logOut();
    this.router.navigate(['/login']);
  }

  doga() {
    this.router.navigate(['/mapa/-1']);
  }

}
