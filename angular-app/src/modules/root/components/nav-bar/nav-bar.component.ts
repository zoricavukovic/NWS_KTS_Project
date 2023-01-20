import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import {Driver} from "../../../shared/models/user/driver";
import {User} from "../../../shared/models/user/user";
import {ConfigService} from "../../../shared/services/config-service/config.service";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {DriverService} from "../../../shared/services/driver-service/driver.service";
import {SocialAuthService, SocialUser} from "@abacritt/angularx-social-login";
import { BellNotification } from 'src/modules/shared/models/notification/bell-notification';
import { BellNotificationsService } from 'src/modules/shared/services/bell-notifications-service/bell-notifications.service';

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
  authUser: SocialUser;
  isAdmin: boolean;
  isRegular: boolean;
  isDriver: boolean;
  driverActivityStatus: boolean;

  bellNotificationsSubscription: Subscription;
  bellAllSeenSubscription: Subscription;
  bellNotifications: BellNotification[];
  numOfNotifications: number;

  constructor(
    public configService: ConfigService,
    private authService: AuthService,
    private router: Router,
    private driverService: DriverService,
    private toast: ToastrService,
    private authServiceForLogin: SocialAuthService,
    private bellNotificationsService: BellNotificationsService
  ) {
    this.driverActivityStatus = false;
    this.bellNotifications = [];
    this.numOfNotifications = 0;
  }

  ngOnInit(): void {
    this.authServiceForLogin.authState.subscribe((user) => {
      this.authUser = user;
    });
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.loggedUser = user;
        this.isAdmin = this.authService.userIsAdmin();
        this.isRegular = this.authService.userIsRegular();
        this.isDriver = this.authService.userIsDriver();
        if (this.isDriver && this.loggedUser) {
          this.loadDriver();
        } else {
          this.driverService.resetGlobalDriver();
        }
        if (this.loggedUser && this.isRegular) {
          this.loadBellNotifications();
        }
      });

    if (this.isDriver) {
      this.followDriverChanges();
    }
  }

  loadBellNotifications(): void {
    this.bellNotificationsSubscription = this.bellNotificationsService.getBellNotifications(this.loggedUser.id).subscribe(
      res => {
        if (res) {
          this.bellNotifications = res;
          this.loadNumOfNotifications();
        }
      }
    );
  }

  loadNumOfNotifications(): void {
    this.numOfNotifications = this.bellNotificationsService.getNumOfNotifications();
  }

  setAllAsSeen(): void {
    if (this.numOfNotifications > 0)
    this.bellAllSeenSubscription = this.bellNotificationsService.setAllAsSeen(this.loggedUser.id).subscribe(
      res => {
        if (res) {
          this.bellNotifications.filter(notification => notification.seen = true);
          this.numOfNotifications = 0;
        }
      }
    );
  }

  notificationRedirect(bellNotification: BellNotification): void {
    console.log(bellNotification.shouldRedirect)
    if (bellNotification.shouldRedirect) {
      this.router.navigate([bellNotification.redirectId]);
    }
  }

  followDriverChanges(): void {
    this.driverSubscription = this.driverService
      .getGlobalDriver()
      .subscribe(driver => {
        if (driver) {
          this.driverData = driver;
          this.driverActivityStatus = this.driverData.active;
        }
      });
  }

  loadDriver(): void {
    this.driverService.get(this.loggedUser.id).subscribe((response: Driver) => {
      this.driverData = response;
      this.driverActivityStatus = this.driverData.active;
      this.driverService.setGlobalDriver(response);
    });
  }

  changeDriverStatus() {
    this.driverUpdateSubscription = this.driverService
      .updateActivityStatus(
        this.driverService.createDriverUpdateActivityRequest(
          this.driverData.id,
          !this.driverActivityStatus
        )
      )
      .subscribe(
        (response: Driver) => {
          this.driverData = response;
          this.driverActivityStatus = this.driverData.active;
        },
        error => {
          this.driverActivityStatus = !this.driverActivityStatus;
          this.toast.error(error.error, 'Changing activity status failed');
        }
      );
  }

  redirectToEditPage() {
    this.router.navigate(['/serb-uber/user/edit-profile-data']);
  }

  redirectToProfilePage() {
    this.router.navigate([`/serb-uber/user/user-profile/${this.loggedUser.id}`]);
  }

  redirectToMessagesPage() {
    this.router.navigate(['/serb-uber/admin/messages']);
  }

  redirectToRessetPassword() {
    this.router.navigate([`/serb-uber/user/reset-password/${this.loggedUser.email}`]);
  }

  bellNotificationsDelete(user: User) {
    if (user.role.name === this.configService.ROLE_REGULAR_USER) {
      this.bellNotificationsService.resetBell();
      this.bellNotificationsSubscription = this.bellNotificationsService.delete(user.id).subscribe(
        res => {
          console.log(res);
        }
      );
    }
  }

  logOut() {
    this.logoutSubscription = this.authService.setOfflineStatus().subscribe(
      response => {
        this.bellNotificationsDelete(response);
        this.authService.logOut();
        this.driverData = null;
        this.driverService.resetGlobalDriver();
        this.router.navigate(['/serb-uber/auth/login']);
        if (this.authUser){
          this.authServiceForLogin.signOut();
        }
      },
      error => {
        this.toast.error(error.error, 'Cannot log out!');
      }
    );
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }

    if (this.driverSubscription) {
      this.driverSubscription.unsubscribe();
      this.driverService.resetGlobalDriver();
    }

    if (this.bellAllSeenSubscription) {
      this.bellAllSeenSubscription.unsubscribe();
      this.bellNotificationsService.resetBell();
    }
  }
}
