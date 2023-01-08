import { Component, Input } from '@angular/core';
import { User } from 'src/app/model/user/user';
import { ConfigService } from 'src/app/service/config.service';
import { Router } from '@angular/router';
import { RegularUser } from 'src/app/model/user/regular-user';

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
    this.router.navigate(['/drivings', this.user.id]);
  }

  goToUserProfile(): void {
    this.router.navigate([`/user-profile/${this.user?.id}`]);
  }

  getBase64Prefix(): string {
    return this.configService.base64_show_photo_prefix;
  }
}
