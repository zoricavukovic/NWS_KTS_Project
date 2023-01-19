import { DatePipe } from '@angular/common';
import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { map, Observable, Subscription } from 'rxjs';
import { AuthService } from 'src/modules/auth/services/auth-service/auth.service';
import { ChartData, ChartSumData } from 'src/modules/shared/models/chart/chart-data';
import { User } from 'src/modules/shared/models/user/user';
import { ConfigService } from 'src/modules/shared/services/config-service/config.service';
import { DrivingService } from 'src/modules/shared/services/driving-service/driving.service';
import { UserService } from 'src/modules/shared/services/user-service/user.service';


@Component({
  selector: 'app-report-tab',
  templateUrl: './report-tab.component.html',
  styleUrls: ['./report-tab.component.css']
})
export class ReportTabComponent implements OnInit, OnDestroy {

  @Input() selectedReport: string;

  loggedUser: User;
  authSubscription: Subscription;
  drivingSubscription: Subscription;
  userSubscription: Subscription;

  isSpending: boolean;
  isDistance: boolean;
  isRide: boolean;
  isAdmin: boolean;
  isRegular: boolean;

  startDate: string;
  endDate: string;
  dateRange: string;
  chartData: ChartData;
  allUsersObj: User[];
  tempListOfUserEmails: string[];
  selectedUserId: number;

  dates = new FormGroup({
    starting: new FormControl(new Date(this.configService.YEAR, this.configService.MONTH, 1)),
    ending: new FormControl(new Date(this.configService.YEAR, this.configService.MONTH, 25)),
  });

  constructor(
    private configService: ConfigService,
    private authService: AuthService,
    private drivingService: DrivingService,
    private datePipe: DatePipe,
    private toast: ToastrService,
    private userService: UserService
  ) { 
    this.selectedReport = '';
    this.loggedUser = null;
    this.startDate = '';
    this.endDate = '';
    this.chartData = null;
    this.isAdmin = false;
    this.isRegular = false;
    this.dateRange = '';
    this.allUsersObj = [];
    this.tempListOfUserEmails = ['All users'];
    this.selectedUserId = -1;
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
          this.isRegular = this.authService.userIsRegular();
          this.loadData();
          if (this.isAdmin) {
            this.loadAllUsers();
          }
        }
      }
    );
  }

  loadData(): void {
    this.startDate = this.datePipe.transform(this.dates.get("starting").value, 'yyyy-MM-dd');
    this.endDate = this.datePipe.transform(this.dates.get("ending").value, 'yyyy-MM-dd');
    this.dateRange = this.getDateRange();

    if (!this.isAdmin) {
      this.drivingSubscription = this.drivingService.getChartData(
        this.drivingService.createChartRequest(this.loggedUser.id, this.selectedReport, this.startDate, this.endDate)
      ).subscribe(
        res => {
          this.chartData = res;
        },
        err => {
          this.toast.error("Chart cannot be shown right now, please try later.","Error happened!");
        }
      );
    } else {
      this.drivingSubscription = this.drivingService.getAdminChartData(
        this.drivingService.createChartRequest(this.selectedUserId, this.selectedReport, this.startDate, this.endDate)
      ).subscribe(
        res => {
          this.chartData = res;
        },
        err => {
          this.toast.error("Chart cannot be shown right now, please try later.","Error happened!");
        }
      );
    }
  }

  private getDateRange(): string {
    const startDate = this.datePipe.transform(this.dates.get("starting").value, 'dd.MM.yyyy.');
    const endDate = this.datePipe.transform(this.dates.get("ending").value, 'dd.MM.yyyy.');
    return `${startDate} to ${endDate}`;
  }

  loadAllUsers() {
    this.userSubscription = this.userService.getAllVerified().subscribe(
      res => {
        for (let user of res) {
          this.tempListOfUserEmails.push(user.email);
          this.allUsersObj.push(user);
        }
      }
    );
  }

  showBySpecificUser(email: string) {
    if (email === 'All users') {
      this.selectedUserId = -1;
    } else {
      for (let user of this.allUsersObj) {
        if (user.email === email) {
          this.selectedUserId = user.id;
        }
      }
    }
  }
  
  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }

    if (this.drivingSubscription) {
      this.drivingSubscription.unsubscribe();
    }

    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

}

