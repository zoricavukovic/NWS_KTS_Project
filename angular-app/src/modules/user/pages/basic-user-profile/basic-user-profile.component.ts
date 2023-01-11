import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
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

@Component({
  selector: 'app-basic-user-profile',
  templateUrl: './basic-user-profile.component.html',
  styleUrls: ['./basic-user-profile.component.css'],
})
export class BasicUserProfileComponent implements OnInit, OnDestroy {
  userId = -1;
  user: User = null;
  userBlocked: boolean;

  userSubscription: Subscription;
  blockedUserSubscription: Subscription;
  blockUserSubscription: Subscription;
  unblockUserSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private authService: AuthService,
    private dialogBlockReason: MatDialog,
    private toast: ToastrService,
    private configService: ConfigService
  ) {
    this.userBlocked = false;
  }

  ngOnInit(): void {
    this.userId = +this.route.snapshot.paramMap.get('id');
    this.userSubscription = this.userService.get(this.userId).subscribe(
      (user: User) => {
        this.user = user;
        if (user && this.loggedUserIsAdmin()) {
          this.loadBlocked();
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

  loadBlocked(): void {
    this.blockedUserSubscription = this.userService
      .getBlockedData(this.user.id, this.userIsDriver())
      .subscribe(response => {
        if (response) {
          this.userBlocked = response;
        }
      });
  }

  userIsDriver(): boolean {
    return this.user?.role.name === this.configService.ROLE_DRIVER;
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
        console.log(JSON.stringify(blockNotification));
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
  }
}
