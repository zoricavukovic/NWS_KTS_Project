import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import {AuthService} from "../../../auth/services/auth-service/auth.service";

@Component({
  selector: 'app-payment-status',
  templateUrl: './payment-status.component.html',
  styleUrls: ['./payment-status.component.css']
})
export class PaymentStatusComponent implements OnInit, OnDestroy {

  showSuccess: boolean;
  loggedUserSubscription: Subscription;
  loggedUserId: number;

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) {
    this.showSuccess = false;
    this.loggedUserId = -1;
  }

  ngOnInit(): void {
    const numIndicator: number = +this.activatedRoute.snapshot.paramMap.get('status');
    this.loggedUserSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        if (user) {
          this.loggedUserId = user.id;
        }
      }
    );

    this.showSuccess = numIndicator && numIndicator === 1;
  }

  goToBalance(): void {
    this.router.navigate([`/serb-uber/regular-user/payment/financial-card/${this.loggedUserId}`]);
  }

  ngOnDestroy(): void {
    if (this.loggedUserSubscription) {
      this.loggedUserSubscription.unsubscribe();
    }
  }

}
