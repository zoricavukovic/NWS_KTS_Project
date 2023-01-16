import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/modules/auth/services/auth-service/auth.service';
import { User } from 'src/modules/shared/models/user/user';
import { ConfigService } from 'src/modules/shared/services/config-service/config.service';
import { DriverService } from 'src/modules/shared/services/driver-service/driver.service';
import { RegularUserService } from 'src/modules/shared/services/regular-user-service/regular-user.service';

@Component({
  selector: 'app-report-tab',
  templateUrl: './report-tab.component.html',
  styleUrls: ['./report-tab.component.css']
})
export class ReportTabComponent implements OnInit, OnDestroy {

  @Input() selectedReport: string;

  loggedUser: User;
  authSubscription: Subscription;
  regularUserSubscription: Subscription;
  driverSubscription: Subscription;

  isSpending: boolean;
  isDistance: boolean;
  isRide: boolean;
  isAdmin: boolean;

  dates = new FormGroup({
    starting: new FormControl(new Date(this.configService.YEAR, this.configService.MONTH, 1)),
    ending: new FormControl(new Date(this.configService.YEAR, this.configService.MONTH, 25)),
  });

  constructor(
    private configService: ConfigService,
    private authService: AuthService,
    private regularUserService: RegularUserService,
    private driverService: DriverService
  ) { 
    this.selectedReport = '';
    this.loggedUser = null;
  }
  
  ngOnInit(): void {
    this.authSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        if (user) {
          this.loggedUser = user;
          this.isSpending = this.selectedReport === this.configService.SELECTED_SPENDING_REPORT;
          this.isDistance = this.selectedReport === this.configService.SELECTED_DISTANCE_REPORT;
          this.isRide = this.selectedReport === this.configService.SELECTED_RIDES_REPORT;
          this.isAdmin = this.authService.userIsAdmin();
          this.loadData();
        }
      }
    );
  }

  loadData(): void {

  }
  
  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }

    if (this.regularUserSubscription) {
      this.regularUserSubscription.unsubscribe();
    }

    if (this.driverSubscription) {
      this.driverSubscription.unsubscribe();
    }
  }

}

