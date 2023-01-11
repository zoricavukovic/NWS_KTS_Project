import { Component, OnInit, OnDestroy } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import {User} from "../../../shared/models/user/user";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {ConfigService} from "../../../shared/services/config-service/config.service";
import {ChangeProfilePicComponent} from "../../../shared/components/change-profile-pic/change-profile-pic.component";

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css'],
})
export class ProfilePageComponent implements OnInit, OnDestroy {
  loggedUser: User;
  authSubscription: Subscription;

  hidePassword: boolean;
  hideConfirmPassword: boolean;

  showEditPassword: boolean;

  constructor(
    private authService: AuthService,
    private configService: ConfigService,
    private dialogEditPicture: MatDialog,
    private router: Router
  ) {
    this.hidePassword = true;
    this.hideConfirmPassword = true;
    this.showEditPassword = false;
  }

  ngOnInit(): void {
    this.authSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        this.loggedUser = user
      }
    );
  }

  showEditProfile(): void {
    this.router.navigate(['/serb-uber/user/edit-profile-data']);
  }

  showChangePhotoDialog(): void {
    const dialogRef = this.dialogEditPicture.open(ChangeProfilePicComponent, {
      data: this.loggedUser.email,
    });

    dialogRef.afterClosed().subscribe(base64 => {
      if (base64) {
        this.loggedUser.profilePicture = base64;
        this.authService.setUserInLocalStorage(this.loggedUser);
      }
    });
  }

  checkValidDialogChange(changedUser: User): boolean {
    return (
      changedUser !== null &&
      changedUser !== undefined &&
      changedUser.role.name !== this.configService.ROLE_DRIVER
    );
  }

  getRole(): string {
    if (
      this.loggedUser.role.name !== null &&
      this.loggedUser.role.name !== this.configService.ROLE_REGULAR_USER
    ) {
      return this.loggedUser.role.name.split('ROLE_')[1];
    } else if (this.loggedUser.role.name !== null) {
      return this.loggedUser.role.name.split('_')[1];
    } else {
      return '';
    }
  }

  getBase64Prefix(): string {
    return this.configService.BASE64_PHOTO_PREFIX;
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }
}