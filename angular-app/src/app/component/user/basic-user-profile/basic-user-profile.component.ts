import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from 'src/app/model/user/user';
import { UserService } from 'src/app/service/user.service';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/service/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmBlockingDialogComponent } from '../confirm-blocking-dialog/confirm-blocking-dialog.component';


@Component({
  selector: 'app-basic-user-profile',
  templateUrl: './basic-user-profile.component.html',
  styleUrls: ['./basic-user-profile.component.css']
})
export class BasicUserProfileComponent implements OnInit, OnDestroy {

  userId: string = "";
  user: User = null;

  showReviews: boolean = true;

  userSubscription: Subscription;
  blockUserSubscription: Subscription;

  ROLE_DRIVER: string = "ROLE_DRIVER";
  ROLE_REGULAR: string = "ROLE_REGULAR_USER";

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private authService: AuthService,
    private dialogBlockReason: MatDialog,
    private toast: ToastrService,  
  ) { }

  ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('id');
    this.userSubscription = this.userService.getUser(this.userId).subscribe(
      (user: User) => {
        this.user = user
      },
      error => {
        this.toast.error(`User with id ${this.userId} not found.`, 'User not found');
      }
    );
  }

  userIsDriver(): boolean {
    
    return this.user?.role.name === this.ROLE_DRIVER;
  }

  loggedUserIsAdmin(): boolean {

    return this.authService.userIsAdmin();
  }

  blockUser(): void {
    let dialogRef = this.dialogBlockReason.open(ConfirmBlockingDialogComponent, {
      data: this.user,
    });

    dialogRef.afterClosed().subscribe(blockNotification => {
      if (blockNotification) {
        console.log(JSON.stringify(blockNotification))
        this.blockUserSubscription = this.userService.blockUser(blockNotification).subscribe(
          res => {
            this.toast.success(`User with id ${this.userId} is successfully blocked.`, 'User blocked!');
          }, 
          error => {
            this.toast.error(error.error, 'User cannot be blocked!');
          });
      }
    });
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }

    if (this.blockUserSubscription) {
      this.blockUserSubscription.unsubscribe();
    }
  }

}
