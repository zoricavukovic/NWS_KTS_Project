import { Component, OnInit, Inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { User } from 'src/app/model/user';
import { AuthService } from 'src/app/service/auth.service';
import { ConfigService } from 'src/app/service/config.service';
import { ChangeProfilePicComponent } from '../change-profile-pic/change-profile-pic.component';
import { EditProfileComponent } from '../edit-profile/edit-profile.component';

@Component({
  selector: 'app-profile-page',
  templateUrl: './profile-page.component.html',
  styleUrls: ['./profile-page.component.css']
})
export class ProfilePageComponent implements OnInit {
  loggedUser: User = this.authService.getCurrentUser();
  
  showPayments: boolean = !this.authService.userIsAdmin();
  hidePassword: boolean =true;
  hideConfirmPassword: boolean =true;

  showEditPassword: boolean = false;
  showEditPayments: boolean = false;

  constructor(
    private authService: AuthService,
    private configService: ConfigService,
    private dialogEditProfile: MatDialog,
    private dialogEditPicture: MatDialog   
    ) {
    
  }
  
  showEditProfileDialog(): void {
    let dialogRef = this.dialogEditProfile.open(EditProfileComponent, {data: this.loggedUser});

    dialogRef.afterClosed().subscribe(res => {
      if (this.checkValidDialogChange(res)) {
        this.loggedUser = res;
        this.authService.setUSerInLocalStorage(res);
      }
    });
  }

  showChangePhotoDialog(): void {
    let dialogRef = this.dialogEditPicture.open(ChangeProfilePicComponent, {data: this.loggedUser.email});

    dialogRef.afterClosed().subscribe(base64 => {
      if (base64 !== null && base64 !== undefined) {
        this.loggedUser.profilePicture = base64;
        this.authService.setUSerInLocalStorage(this.loggedUser);
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

  ngOnInit(): void {
  }

}
