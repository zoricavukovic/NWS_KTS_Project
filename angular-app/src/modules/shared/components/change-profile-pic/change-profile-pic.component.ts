import { Component, Inject, OnDestroy } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable, ReplaySubject, Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import {ConfigService} from "../../services/config-service/config.service";
import {UserService} from "../../services/user-service/user.service";
import {UserProfilePictureRequest} from "../../models/user/user-profile-update";

@Component({
  selector: 'app-change-profile-pic',
  templateUrl: './change-profile-pic.component.html',
  styleUrls: ['./change-profile-pic.component.css'],
})
export class ChangeProfilePicComponent implements OnDestroy {
  base64Var: string;
  imageChanged: boolean;

  profilePicUpdateSubscription: Subscription;

  constructor(
    private configService: ConfigService,
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) public email: string,
    private toast: ToastrService
  ) {
    this.imageChanged = false;
  }

  onFileSelected(event): void {
    this.convertFile(event.target.files[0]).subscribe(base64 => {
      this.base64Var = base64;
      this.imageChanged = true;
    });
  }

  convertFile(file: File): Observable<string> {
    const result = new ReplaySubject<string>(1);
    const reader = new FileReader();
    reader.readAsBinaryString(file);
    reader.onload = event => result.next(btoa(event.target.result.toString()));
    return result;
  }

  saveChanges() {
    const updateProfilePicture: UserProfilePictureRequest = {
      email: this.email,
      profilePicture: this.base64Var,
    };
    this.profilePicUpdateSubscription = this.userService
      .updateProfilePicture(updateProfilePicture)
      .subscribe(
        res =>
          this.toast.success(
            'Profile picture is changed successfully!',
            'Profile picture changed'
          ),
        error =>
          this.toast.error(
            'Update failed, please select image!',
            'Profile picture changing failed'
          )
      );
  }

  getBase64Prefix(): string {
    return this.configService.BASE64_PHOTO_PREFIX;
  }

  ngOnDestroy(): void {
    if (this.profilePicUpdateSubscription) {
      this.profilePicUpdateSubscription.unsubscribe();
    }
  }
}
