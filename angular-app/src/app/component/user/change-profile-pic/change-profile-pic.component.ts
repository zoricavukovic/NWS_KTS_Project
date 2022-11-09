import { Component, Inject, OnDestroy, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable, ReplaySubject, Subscription } from 'rxjs';
import { UserProfilePictureRequest } from 'src/app/model/request/user/user-profile-update';
import { ConfigService } from 'src/app/service/config.service';
import { UserService } from 'src/app/service/user.service';
import { NgToastService } from 'ng-angular-popup';

@Component({
  selector: 'app-change-profile-pic',
  templateUrl: './change-profile-pic.component.html',
  styleUrls: ['./change-profile-pic.component.css']
})
export class ChangeProfilePicComponent implements OnInit, OnDestroy {

  base64Var: string;
  imageChanged: boolean = false;

  profilePicUpdateSubscription: Subscription;

  constructor(
    private configService: ConfigService,
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) public email: string,
    private toast: NgToastService
  ) { }

  ngOnInit(): void {
  }

  onFileSelected(event): void {
    this.convertFile(event.target.files[0]).subscribe(base64 => {
      this.base64Var = base64;
      this.imageChanged = true;
    });
  }

  convertFile(file : File) : Observable<string> {
    const result = new ReplaySubject<string>(1);
    const reader = new FileReader();
    reader.readAsBinaryString(file);
    reader.onload = (event) => result.next(btoa(event.target.result.toString()));
    return result;
  }

  saveChanges() {

    this.profilePicUpdateSubscription = this.userService.updateProfilePicture(
      new UserProfilePictureRequest(
        this.email,
        this.base64Var
      )
    ).subscribe(
      res => this.toast.success({detail:"Profile picture changed", summary:"Profile picture is changed successfully!", 
              duration:4000, position:'bl'}), 
      error => this.toast.error({detail:"Profile picture change failed", summary:"Update failed, please select image!", 
              duration:4000, position:'bl'})
    );
  }

  getBase64Prefix(): string {
    return this.configService.base64_show_photo_prefix;
  }

  ngOnDestroy(): void {
    if (this.profilePicUpdateSubscription) {
      this.profilePicUpdateSubscription.unsubscribe();
    }
  }

}
