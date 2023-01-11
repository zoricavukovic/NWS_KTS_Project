import { Component, Input } from '@angular/core';
import {ConfigService} from "../../services/config-service/config.service";
import {User} from "../../models/user/user";

@Component({
  selector: 'app-basic-user-data',
  templateUrl: './basic-user-data.component.html',
  styleUrls: ['./basic-user-data.component.css']
})
export class BasicUserDataComponent {

  @Input() user: User;

  constructor(public configService: ConfigService) {
    this.user = null;
  }

  getRoleName() {
    return (this.user?.role?.name === "ROLE_DRIVER") ? "DRIVER" : "REGULAR";
  }

}
