import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { Driver } from 'src/app/model/user/driver';
import { AuthService } from 'src/app/service/auth.service';
import { ConfigService } from 'src/app/service/config.service';
import { DriverService } from 'src/app/service/driver.service';
import { ToastrService } from 'ngx-toastr';


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
    public configService: ConfigService,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.isAdmin = this.authService.getCurrentUser?.isUserAdmin();
    this.isRegularUser = this.authService.getCurrentUser?.userIsRegular();
    
    if (this.authService.getCurrentUser?.userIsDriver()) {
      this.loadDriver();
    }
  }

  loadDriver(): void {
    this.driverService.getDriver(this.authService.getCurrentUser.id)
      .subscribe(
        (response: Driver) => {
          console.log("PODACI" + response);
          this.driverData = response;
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

  loggedUserIsDriver(): boolean {
    //ako je driver null ucitaj ga, ako je u medjuvremenu ulogovan neko drugi, osvezi
    if (this.driverData) {
      if (this.authService.getCurrentUser?.userIsDriver() && this.isUserChanged()) {
        this.loadDriver();
      }
    } else if (!this.driverData && this.authService.getCurrentUser?.userIsDriver()){
      this.loadDriver();
    }
    
    return this.driverData && this.authService.getCurrentUser.userIsDriver();
  }

  isUserChanged(): boolean {
    
    return this.authService.getCurrentUser?.email !== this.driverData?.email;
  }

  logOut() {
    this.logoutSubscription = this.authService
      .setOfflineStatus()
      .subscribe(response => {
        this.authService.logOut();
        this.driverData = null;
        this.router.navigate(['/login']);
      }, error => {
        this.toast.error(error.error, 'Cannot log out!');
      });
  }

  doga() {
    this.router.navigate(['/mapa/-1']);
  }

}
