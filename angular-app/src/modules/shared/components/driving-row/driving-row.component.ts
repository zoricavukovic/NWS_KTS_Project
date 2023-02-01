import {
  Component,
  OnInit,
  Input,
  OnDestroy,
  Output,
  EventEmitter,
} from '@angular/core';
import { MatDialog, MatDialogConfig } from '@angular/material/dialog';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { Driving } from '../../models/driving/driving';
import { User } from '../../models/user/user';
import { RatingDialogComponent } from '../rating-dialog/rating-dialog.component';
import { RegularUserService } from '../../services/regular-user-service/regular-user.service';
import { AuthService } from 'src/modules/auth/services/auth-service/auth.service';

@Component({
  selector: 'driving-row',
  templateUrl: './driving-row.component.html',
  styleUrls: ['./driving-row.component.css'],
})
export class DrivingRowComponent implements OnInit, OnDestroy {
  @Input() driving: Driving;
  @Input() user: User;
  @Output() favouriteRouteChangeEmit = new EventEmitter();
  isRegularUser = true;
  favouriteRoute = false;

  reviewSubscription: Subscription;
  favouriteRouteSubscription: Subscription;

  constructor(
    private router: Router,
    private dialog: MatDialog,
    private toast: ToastrService,
    private regularUserService: RegularUserService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    this.isRegularUser = this.authService.userIsRegular();
  }

  goToDetailsPage(id: number) {
    this.router.navigate([`/serb-uber/user/map-page-view/${id}`]);
  }

  isDisabledBtnRate(date): boolean {
    const date_today: Date = new Date();

    return !(this.getDifferenceInDays(date, date_today) > 3);
  }

  getDifferenceInDays(date1, date2): number {
    const diffInMs = Math.abs(date2 - Date.parse(date1));

    return diffInMs / (1000 * 60 * 60 * 24);
  }

  endDrivingDate(startDate, duration) {
    const start = new Date(Date.parse(startDate));

    return new Date(start.getTime() + duration * 60000);
  }

  openDialog(drivingId: number) {
    const dialogConfig = new MatDialogConfig();

    dialogConfig.disableClose = true;
    dialogConfig.autoFocus = true;
    dialogConfig.data = {
      drivingId: drivingId,
      userId: this.user.id,
    };
    const dialogRef = this.dialog.open(RatingDialogComponent, dialogConfig);

    dialogRef.afterClosed().subscribe(
      response => {
        if (response !== null) {
          this.toast.success('Review created');
          this.driving.hasReviewForUser = true;
        }
      },
      error => {
        this.toast.error('Review creation failed');
      }
    );
  }

  ngOnDestroy(): void {
    if (this.reviewSubscription) {
      this.reviewSubscription.unsubscribe();
    }
    if (this.favouriteRouteSubscription) {
      this.favouriteRouteSubscription.unsubscribe();
    }
  }
}
