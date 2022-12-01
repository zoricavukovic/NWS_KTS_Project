import { Component, Input, OnInit } from '@angular/core';
import { User } from 'src/app/model/user/user';
import { ConfigService } from 'src/app/service/config.service';

@Component({
  selector: 'app-basic-user-data',
  templateUrl: './basic-user-data.component.html',
  styleUrls: ['./basic-user-data.component.css']
})
export class BasicUserDataComponent implements OnInit {

  @Input() user: User;

  constructor(public configService: ConfigService) {
    this.user = null;
  }

  ngOnInit(): void {
  }

  getRoleName() {
    return (this.user?.role?.name === "ROLE_DRIVER") ? "DRIVER" : "REGULAR";
  }

}
