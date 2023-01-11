import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import {RegularUser} from "../../../shared/models/user/regular-user";
import {ConfigService} from "../../../shared/services/config-service/config.service";

@Component({
  selector: 'user-row',
  templateUrl: './user-row.component.html',
  styleUrls: ['./user-row.component.css'],
})
export class UserRowComponent {
  @Input() user: RegularUser;
  @Input() index: number;

  constructor(private configService: ConfigService, private router: Router) {}

  viewDrivings(): void {
    this.router.navigate(['/serb-uber/drivings', this.user.id]);
  }

  goToUserProfile(): void {
    this.router.navigate([`/serb-uber/user/user-profile/${this.user?.id}`]);
  }

  getBase64Prefix(): string {
    return this.configService.BASE64_PHOTO_PREFIX;
  }
}
