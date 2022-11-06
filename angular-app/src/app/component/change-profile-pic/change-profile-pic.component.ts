import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { Observable, ReplaySubject } from 'rxjs';
import { UserProfilePictureRequest } from 'src/app/model/user-profile-picture-request';
import { ConfigService } from 'src/app/service/config.service';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-change-profile-pic',
  templateUrl: './change-profile-pic.component.html',
  styleUrls: ['./change-profile-pic.component.css']
})
export class ChangeProfilePicComponent implements OnInit {

  base64Var: string;
  imageChanged: boolean = false;

  constructor(
    private configService: ConfigService,
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) public email: string
  ) { }

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
    this.userService.updateProfilePicture(
      new UserProfilePictureRequest(
        this.email,
        this.base64Var
      )
    );
  }

  reset(): void {
    if (this.imageChanged) {
      this.base64Var = "";
      this.imageChanged = false;
    } else {
      this.imageChanged = true;
    }
  }

  ngOnInit(): void {
  }

  getBase64Prefix(): string {
    return this.configService.base64_show_photo_prefix;
  }

}
