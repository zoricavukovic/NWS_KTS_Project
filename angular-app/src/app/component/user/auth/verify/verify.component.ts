import { Component, OnInit, OnDestroy } from '@angular/core';
import { VerifyService } from 'src/app/service/verify.service';
import { Subscription } from 'rxjs';
import { VerifyRequest } from 'src/app/model/request/verify-request';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthService } from 'src/app/service/auth.service';
import { NgToastService } from 'ng-angular-popup';


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
  showForm: boolean = true;
  MAX_DIGIT_LENGTH: number = 4;

  verifySubscription: Subscription;
  sendCodeAgainSubscription: Subscription;

  constructor(
    private verifyService: VerifyService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private toast: NgToastService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.verifyId = this.route.snapshot.paramMap.get('id');
  }

  getVerifyUserType(): string {
    if (this.authService.userIsAdmin()){

      return "ROLE_DRIVER";
    } else {

      return "ROLE_REGULAR_USER";
    }
  }

  containsOnlyNumbers(str: string) {
    return /^\d+$/.test(str);
  }

  checkValidationCode(): boolean {
    let securityCode: string = this.firstDigit + this.secondDigit + this.thirdDigit + this.fourthDigit;
    if (securityCode.length !== this.MAX_DIGIT_LENGTH) {
      this.toast.error({detail:"Error", summary:"You need to add 4 digits.", 
              duration:4000, position:'bl'})

      return false
    } else if (!this.containsOnlyNumbers(securityCode)) {
      this.toast.error({detail:"Error", summary:"You can input only digits!", 
              duration:4000, position:'bl'})

      return false;
    } else if (!this.containsOnlyNumbers(this.verifyId)) {
      this.toast.error({detail:"Error", summary:"You changed url!", 
              duration:4000, position:'bl'})

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
        this.getVerifyUserType()
      )).subscribe(
        res => {
          this.toast.success({detail:"Verification successfull", summary:"You are verified!", 
              duration:4000, position:'bl'})
          this.router.navigate(['/login'])
        }, 
        error => this.toast.error({detail:"Verification failed", summary:error.error, 
              duration:4000, position:'bl'})
      );
    }
  }

  sendCodeAgain() {
    if (this.containsOnlyNumbers(this.verifyId)){
      this.sendCodeAgainSubscription = this.verifyService.sendCodeAgain(Number(this.verifyId)).subscribe(
        res => this.showForm = !this.showForm, 
        error => this.toast.error({detail:"Code cannot be sent", summary:"Email cannot be sent.", 
              duration:4000, position:'bl'})
    );
    } else {
      this.toast.error({detail:"Code cannot be sent", summary:"You changed url, verify id is wrong.", 
              duration:4000, position:'bl'})
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
