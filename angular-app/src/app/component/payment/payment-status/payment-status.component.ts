import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-payment-status',
  templateUrl: './payment-status.component.html',
  styleUrls: ['./payment-status.component.css']
})
export class PaymentStatusComponent implements OnInit, OnDestroy {

  constructor(
    private activatedRoute: ActivatedRoute,
    private router: Router,
    private authService: AuthService
  ) { }

  showSuccess: boolean = false;
  loggedUserSubscription: Subscription;
  loggedUserId: number = -1;

  ngOnInit(): void {
    let numIndicator: number = +this.activatedRoute.snapshot.paramMap.get('status');
    this.loggedUserSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        if (user) {
          this.loggedUserId = user.id;
        }
      }
    );

    if (numIndicator && numIndicator === 1) {
      this.showSuccess = true;
    }
    else {
      this.showSuccess = false;
    } 
  }

  goToBalance(): void {
    this.router.navigate([`payment/financial-card/${this.loggedUserId}`]);
  }

  ngOnDestroy(): void {
    if (this.loggedUserSubscription) {
      this.loggedUserSubscription.unsubscribe();
    }
  }

}
