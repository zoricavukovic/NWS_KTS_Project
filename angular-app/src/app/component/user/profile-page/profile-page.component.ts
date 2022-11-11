import { Component, OnInit, Inject, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Navigation, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/response/user/user';
import { AuthService } from 'src/app/service/auth.service';
import { ConfigService } from 'src/app/service/config.service';
import { ChangeProfilePicComponent } from '../change-profile-pic/change-profile-pic.component';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit, OnDestroy {

  loggedUser: User;
  authSubscription: Subscription;

  showPayments: boolean = !this.authService.userIsAdmin();
  hidePassword: boolean =true;
  hideConfirmPassword: boolean =true;

  showEditPassword: boolean = false;
  showEditPayments: boolean = false;

  constructor(
    private authService: AuthService,
    private configService: ConfigService,
    private dialogEditPicture: MatDialog,
    private router: Router
    ) {

  }

  ngOnInit(): void {
    this.authSubscription = this.authService.getCurrentUser().subscribe(
      user => this.loggedUser = user
    );
  }

  showEditProfile(): void {
    this.router.navigate(['/edit-profile-data']);
  }

  showChangePhotoDialog(): void {
    let dialogRef = this.dialogEditPicture.open(ChangeProfilePicComponent, {data: this.loggedUser.email});

    dialogRef.afterClosed().subscribe(base64 => {
      if (base64) {
        this.loggedUser.profilePicture = base64;
        this.authService.setUserInLocalStorage(this.loggedUser);
      }
    });
  }

  checkValidDialogChange(changedUser: User): boolean {
    return (changedUser !== null && changedUser !== undefined && changedUser.role.name !== this.configService.role_driver)
  }

  getRole(): string {
    if (this.loggedUser.role.name !== null && this.loggedUser.role.name !== this.configService.role_regular_user) {
      return this.loggedUser.role.name.split("ROLE_")[1]
    } else if (this.loggedUser.role.name !== null) {
      return this.loggedUser.role.name.split("_")[1]
    } else {
      return "";
    }
  }

  getBase64Prefix(): string {
    return this.configService.base64_show_photo_prefix
  }

   ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }


}
