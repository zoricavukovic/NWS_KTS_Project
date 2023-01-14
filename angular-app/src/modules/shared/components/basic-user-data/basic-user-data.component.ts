import { Component, Input, OnDestroy } from '@angular/core';
import {ConfigService} from "../../services/config-service/config.service";
import {User} from "../../models/user/user";
import { MatDialog } from '@angular/material/dialog';
import { ChangeProfilePicComponent } from '../change-profile-pic/change-profile-pic.component';
import { AuthService } from 'src/modules/auth/services/auth-service/auth.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-basic-user-data',
  templateUrl: './basic-user-data.component.html',
  styleUrls: ['./basic-user-data.component.css']
})
export class BasicUserDataComponent implements OnDestroy {

  @Input() user: User;

  @Input() currentUserIsLogged: boolean;

  @Input() isDriver: boolean;

  authSubscription: Subscription;

  constructor(
    public configService: ConfigService,
    private dialogEditPicture: MatDialog,
    private authService: AuthService
  ) {
    this.user = null;
    this.currentUserIsLogged = false;
    this.isDriver = false;
  }

  getRoleName() {
    return (this.user?.role?.name === "ROLE_DRIVER") ? "DRIVER" : "REGULAR";
  }

  showChangePhotoDialog(): void {
    if (this.currentUserIsLogged) {
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
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }
  }

}
