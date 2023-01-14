import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from '../../models/user/user';
import { ConfigService } from '../../services/config-service/config.service';
import { DriverService } from '../../services/driver-service/driver.service';

@Component({
  selector: 'app-user-profile-info',
  templateUrl: './user-profile-info.component.html',
  styleUrls: ['./user-profile-info.component.scss']
})
export class UserProfileInfoComponent implements OnInit, OnDestroy {

  @Input() user: User; 

  @Input() profilePage: boolean;

  driverRate: number;
  driverRateSubscription: Subscription;

  constructor(
    public configService: ConfigService,
    private driverService: DriverService
  ) {
    this.driverRate = 0;
    this.profilePage = false;
    this.user = null;
  }

  ngOnInit(): void {
    if (this.user && this.user.role.name === this.configService.ROLE_DRIVER) {
      this.driverRateSubscription = this.driverService.getDriverRate(this.user.id).subscribe(
        res => {
          this.driverRate = res;
        }
      );
    }
  }

  ngOnDestroy(): void {
    if (this.driverRateSubscription) {
      this.driverRateSubscription.unsubscribe();
    }
  }

}
