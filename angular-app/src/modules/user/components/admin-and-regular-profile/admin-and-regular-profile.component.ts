import { Component, Input } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { AuthService } from 'src/modules/auth/services/auth-service/auth.service';
import { ChangeProfilePicComponent } from 'src/modules/shared/components/change-profile-pic/change-profile-pic.component';
import { User } from 'src/modules/shared/models/user/user';
import { ConfigService } from 'src/modules/shared/services/config-service/config.service';

@Component({
  selector: 'app-admin-and-regular-profile',
  templateUrl: './admin-and-regular-profile.component.html',
  styleUrls: ['./admin-and-regular-profile.component.css']
})
export class AdminAndRegularProfileComponent {

  @Input() user: User;
  @Input() isRegular: boolean;

  constructor(
    private configService: ConfigService,
    private authService: AuthService,
    private dialogEditPicture: MatDialog,
    private router: Router
  ) {
    this.user = null;
    this.isRegular = false;
  }

  showEditProfile(): void {
    this.router.navigate(['/serb-uber/user/edit-profile-data']);
  }

  showChangePhotoDialog(): void {
    const dialogRef = this.dialogEditPicture.open(ChangeProfilePicComponent, {
      data: this.user.email,
    });

    dialogRef.afterClosed().subscribe(base64 => {
      if (base64) {
        this.user.profilePicture = base64;
        this.authService.setUserInLocalStorage(this.user);
      }
    });
  }

  getRole(): string {
    if (
      this.user.role.name !== null &&
      this.user.role.name !== this.configService.ROLE_REGULAR_USER
    ) {
      return this.user.role.name.split('ROLE_')[1];
    } else if (this.user.role.name !== null) {
      return this.user.role.name.split('_')[1];
    } else {
      return '';
    }
  }

  getBase64Prefix(): string {
    return this.configService.BASE64_PHOTO_PREFIX;
  }

}
