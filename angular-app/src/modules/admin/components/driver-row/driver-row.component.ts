import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { ConfigService } from 'src/modules/shared/services/config-service/config.service';
import {Driver} from "../../../shared/models/user/driver";

@Component({
  selector: 'driver-row',
  templateUrl: './driver-row.component.html',
  styleUrls: ['./driver-row.component.css'],
})
export class DriverRowComponent {
  @Input() driver: Driver;
  @Input() index: number;

  constructor(private configService: ConfigService, private router: Router) {}

  viewDrivings(): void {
    this.router.navigate(['/serb-uber/drivings', this.driver.id]);
  }

  goToDriverProfile(): void {
    this.router.navigate([`/serb-uber/user/user-profile/${this.driver?.id}`]);
  }

  getBase64Prefix(): string {
    return this.configService.BASE64_PHOTO_PREFIX;
  }
}