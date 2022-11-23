import { Component, Input } from '@angular/core';
import { Driver } from 'src/app/model/user/driver';
import { ConfigService } from 'src/app/service/config.service';
import { Router } from '@angular/router';

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
    this.router.navigate(['/drivings', this.driver.id]);
  }

  getBase64Prefix(): string {
    return this.configService.base64_show_photo_prefix;
  }
}
