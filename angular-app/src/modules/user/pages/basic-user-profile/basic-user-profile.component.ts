import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {User} from "../../../shared/models/user/user";
import {UserService} from "../../../shared/services/user-service/user.service";
import {ConfigService} from "../../../shared/services/config-service/config.service";
import {
  ConfirmBlockingDialogComponent
} from "../../../shared/components/confirm-blocking-dialog/confirm-blocking-dialog.component";
import { DriverService } from 'src/modules/shared/services/driver-service/driver.service';
import { Driver } from 'src/modules/shared/models/user/driver';
import { BehaviourReportDialogComponent } from 'src/modules/shared/components/behaviour-report-dialog/behaviour-report-dialog.component';

@Component({
  selector: 'app-basic-user-profile',
  templateUrl: './basic-user-profile.component.html',
  styleUrls: ['./basic-user-profile.component.css'],
})
export class BasicUserProfileComponent implements OnInit, OnDestroy {
  userId = -1;
  user: User = null;
  userBlocked: boolean;
  currentUser: User;

  userSubscription: Subscription;
  blockedUserSubscription: Subscription;
  blockUserSubscription: Subscription;
  unblockUserSubscription: Subscription;

  isDriver: boolean;
  isRegular: boolean;
  loggedAdmin: boolean;
  status: boolean;
  currentUserIsLogged: boolean;
  authSubscription: Subscription;
  driverSubscription: Subscription;

  driverStatus: boolean;

  constructor(
    public configService: ConfigService,
    private route: ActivatedRoute,
    private userService: UserService,
    private authService: AuthService,
    private dialogBlockReason: MatDialog,
    private toast: ToastrService,
    private router: Router,
    private driverService: DriverService,
  ) {
    this.userBlocked = false;
    this.isDriver = false;
    this.loggedAdmin = false;
    this.currentUserIsLogged = false;
    this.user = null;
    this.driverStatus = false;
    this.router.routeReuseStrategy.shouldReuseRoute = () => false;
    this.status = false;
    this.currentUser = null;
  }

  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get('id');
    if (this.route.snapshot.paramMap.get('status')) {
      this.status = true;
    }
    this.userSubscription = this.userService.get(this.userId).subscribe(
      (user: User) => {
        this.user = user;
        if (user) {
          this.isDriver = this.userIsDriver();
          this.isRegular = this.userIsRegular();
          this.loggedAdmin = this.loggedUserIsAdmin();
          this.checkIfUserIsCurrent();
          this.loadBlocked();
          if (this.isDriver && this.currentUserIsLogged) {
            this.loadDriver();
          }
        }
      },
      error => {
        this.toast.error(
          `User with id ${this.userId} not found.`,
          'User not found'
        );
      }
    );
  }

  loadDriver(): void {
    this.driverSubscription = this.driverService.get(this.user.id).subscribe((response: Driver) => {
      this.driverStatus = response.active;
    });
  }

  changeDriverStatus() {
    this.driverSubscription = this.driverService
      .updateActivityStatus(
        this.driverService.createDriverUpdateActivityRequest(
          this.user.id,
          !this.driverStatus
        )
      )
      .subscribe(
        (response: Driver) => {
          this.driverStatus = response.active;
          this.driverService.setGlobalDriver(response);
        },
        error => {
          this.driverStatus = !this.driverStatus;
          this.toast.error(error.error, 'Changing activity status failed');
        }
      );
  }

  checkIfUserIsCurrent(): void {
    this.authSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        if (user) {
          this.currentUserIsLogged = user.email === this.user.email
          this.currentUser = user;
        }
      }
    );
  }

  loadBlocked(): void {
    if (this.loggedAdmin && !this.currentUserIsLogged) {
      this.blockedUserSubscription = this.userService
        .getBlockedData(this.user.id, this.userIsDriver())
        .subscribe(response => {
          if (response) {
            this.userBlocked = response;
          }
        });
    }
  }

  showEditProfile(): void {
    this.router.navigate(['/serb-uber/user/edit-profile-data']);
  }

  userIsDriver(): boolean {
    return this.user?.role.name === this.configService.ROLE_DRIVER;
  }

  userIsRegular(): boolean {
    return this.user?.role.name === this.configService.ROLE_REGULAR_USER;
  }

  loggedUserIsAdmin(): boolean {
    return this.authService.userIsAdmin();
  }

  blockUser(): void {
    const dialogRef = this.dialogBlockReason.open(
      ConfirmBlockingDialogComponent,
      {
        data: this.user,
      }
    );

    dialogRef.afterClosed().subscribe(blockNotification => {
      if (blockNotification) {
        this.blockUserSubscription = this.userService
          .blockUser(blockNotification)
          .subscribe(
            res => {
              this.toast.success(
                `User with id ${this.userId} is successfully blocked.`,
                'User blocked!'
              );
              this.userBlocked = true;
            },
            error => {
              this.toast.error(error.error, 'User cannot be blocked!');
            }
          );
      }
    });
  }

  unblockUser(): void {
    if (this.loggedAdmin) {
      this.unblockUserSubscription = this.userService
        .unblockUser(this.user.id, this.userIsDriver())
        .subscribe(
          res => {
            this.toast.success(
              `User with id ${this.userId} is successfully unblocked.`,
              'User unblocked!'
            );
            this.userBlocked = false;
          },
          error => {
            this.toast.error(error.error, 'User cannot be unblocked!');
          }
        );
    }
  }

  reportUser(): void {
    if (this.currentUser?.role.name === this.configService.ROLE_DRIVER) {
      const dialogRef = this.dialogBlockReason.open(BehaviourReportDialogComponent, {
        data: {currentUser: this.currentUser, driver: null, userToReport: this.user},
      });
    }
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }

    if (this.blockUserSubscription) {
      this.blockUserSubscription.unsubscribe();
    }

    if (
      this.blockedUserSubscription !== null &&
      this.blockUserSubscription !== undefined
    ) {
      this.blockUserSubscription.unsubscribe();
    }

    if (this.unblockUserSubscription) {
      this.unblockUserSubscription.unsubscribe();
    }

    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }

    if (this.driverSubscription) {
      this.driverSubscription.unsubscribe();
    }
  }
}
