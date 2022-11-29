import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Driver } from 'src/app/model/user/driver';
import { User } from 'src/app/model/user/user';
import { AuthService } from 'src/app/service/auth.service';
import { ConfigService } from 'src/app/service/config.service';
import { DriverService } from 'src/app/service/driver.service';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css'],
})
export class NavBarComponent implements OnInit, OnDestroy {
  
  logoutSubscription: Subscription;
  driverData: Driver;
  driverUpdateSubscription: Subscription;

  authSubscription: Subscription;
  driverSubscription: Subscription;

  loggedUser: User = null;
  isAdmin: boolean;
  isRegular: boolean;
  isDriver: boolean;

  constructor(
    public configService: ConfigService,
    private authService: AuthService,
    private router: Router,
    private driverService: DriverService,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        this.loggedUser = user;
        this.isAdmin = this.authService.userIsAdmin();
        this.isRegular = this.authService.userIsRegular();
        this.isDriver = this.authService.userIsDriver();
        if (this.isDriver) {
          this.loadDriver();
        } else {
          this.driverService.resetGlobalDriver();
        }
      }
    );

    if (this.isDriver) {
      this.followDriverChanges();
    }
  }

  followDriverChanges(): void {
    this.driverSubscription = this.driverService.getGlobalDriver().subscribe(
      driver => {
        this.driverData = driver;
      }
    )
  }

  loadDriver(): void {
    this.driverService.getDriver(this.loggedUser.id)
      .subscribe(
        (response: Driver) => {
          this.driverData = response;
          this.driverService.setGlobalDriver(response);
      });
  }

  changeDriverStatus() {
    this.driverUpdateSubscription = this.driverService.updateActivityStatus(
      this.driverService.createDriverUpdateActivityRequest(this.driverData.id, !this.driverData.active)
    ).subscribe((response: Driver) => {
      this.driverData = response;
      },
      error => {
        this.driverData.active = !this.driverData.active;
        this.toast.error(error.error, 'Changing activity status failed');
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
        this.authService.logOut();
        this.driverData = null;
        this.driverService.resetGlobalDriver();
        this.router.navigate(['/login']);
      }, error => {
        this.toast.error(error.error, 'Cannot log out!');
      });
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }

    if (this.driverSubscription) {
      this.driverSubscription.unsubscribe();
      this.driverService.resetGlobalDriver();
    }
  }

}
