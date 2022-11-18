import { Component, OnInit, OnDestroy } from '@angular/core';
import { VerifyService } from 'src/app/service/verify.service';
import { Subscription } from 'rxjs';
import { VerifyRequest } from 'src/app/model/request/verify-request';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth.service';
import {ToastrService} from "ngx-toastr";
import { User } from 'src/app/model/response/user/user';

@Component({
  selector: 'app-verify',
  templateUrl: './verify.component.html',
  styleUrls: ['./verify.component.css']
})
export class VerifyComponent implements OnInit, OnDestroy {

  firstDigit: string;
  secondDigit: string;
  thirdDigit: string;
  fourthDigit: string;
  verifyId: string;
  verifyUserType: string;
  showForm: boolean = true;
  MAX_DIGIT_LENGTH: number = 4;

  verifySubscription: Subscription;
  currentUserSubscription: Subscription;
  sendCodeAgainSubscription: Subscription;

  constructor(
    private verifyService: VerifyService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private toast: ToastrService,
    private router: Router
  ) {}

  private checkRegistrationPurpose(user: User) {
    if(user.isUserAdmin()){
      this.verifyUserType = "ROLE_DRIVER";
    }
    else {
      this.verifyUserType = "ROLE_REGULAR_USER";
    }
  }

  ngOnInit(): void {
    this.verifyId = this.route.snapshot.paramMap.get('id');
    this.checkRegistrationPurpose(this.authService.getCurrentUser);
  }

  containsOnlyNumbers(str: string) {
    return /^\d+$/.test(str);
  }

  checkValidationCode(): boolean {
    let securityCode: string = this.firstDigit + this.secondDigit + this.thirdDigit + this.fourthDigit;
    if (securityCode.length !== this.MAX_DIGIT_LENGTH) {
      this.toast.error("You need to add 4 digits.", "Error")

      return false;
    } else if (!this.containsOnlyNumbers(securityCode)) {
      this.toast.error("You can input only digits!", "Error")

      return false;
    } else if (!this.containsOnlyNumbers(this.verifyId)) {
      this.toast.error("Something happened with URL. Check again URL on email.", "Error")

      return false;
    }

    return true;
  }

  verify() {
    if (this.checkValidationCode()) {
      let securityCode: string = this.firstDigit + this.secondDigit + this.thirdDigit + this.fourthDigit;

      this.verifySubscription = this.verifyService.verify(new VerifyRequest(
        Number(this.verifyId),
        Number(securityCode),
        this.verifyUserType
      )).subscribe(
        res => {
          this.toast.success("You are verified!", "Verification successfully")
          this.router.navigate(['/login']);
        },
        error => this.toast.error(error.error, "Verification failed")
      );
    }
  }

  sendCodeAgain() {
    if (this.containsOnlyNumbers(this.verifyId)){
      this.sendCodeAgainSubscription = this.verifyService.sendCodeAgain(Number(this.verifyId)).subscribe(
        res => this.showForm = !this.showForm,
        error => this.toast.error("Email cannot be sent.", "Code cannot be sent")
    );
    } else {
      this.toast.error("Something happened with URL. Check again URL on email.","Code cannot be sent")
    }
  }

  ngOnDestroy(): void {
    if (this.verifySubscription) {
      this.verifySubscription.unsubscribe();
    }

    if (this.sendCodeAgainSubscription)
    this.sendCodeAgainSubscription.unsubscribe();
  }

}
