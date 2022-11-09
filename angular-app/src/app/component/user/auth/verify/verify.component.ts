import { Component, OnInit, OnDestroy } from '@angular/core';
import { VerifyService } from 'src/app/service/verify.service';
import { Subscription } from 'rxjs';
import { VerifyRequest } from 'src/app/model/request/verify-request';
import { ActivatedRoute } from '@angular/router';
import { AuthService } from 'src/app/service/auth.service';


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

  constructor(
    private verifyService: VerifyService,
    private authService: AuthService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.verifyId = this.route.snapshot.paramMap.get('id');
  }

  getVerifyUserType(): string {
    if (this.authService.userIsAdmin()){

      return "ROLE_DRIVER";
    } else {

      return "ROLE_DRIVER";
    }
  }

  containsOnlyNumbers(str: string) {
    return /^\d+$/.test(str);
  }

  checkValidationCode(): boolean {
    let securityCode: string = this.firstDigit + this.secondDigit + this.thirdDigit + this.fourthDigit;
    if (securityCode.length !== this.MAX_DIGIT_LENGTH) {
      alert("You need to input 4 digits");
      return false
    } else if (!this.containsOnlyNumbers(securityCode)) {
      alert("You can input only digits!"); //ovo samo dok nema toastify
      return false;
    }  

    return true;
  }

  verify() {
    if (this.checkValidationCode()) {
      let securityCode: string = this.firstDigit + this.secondDigit + this.thirdDigit + this.fourthDigit;
      
      this.verifySubscription = this.verifyService.verify(new VerifyRequest(
        this.verifyId,
        Number(securityCode),
        this.getVerifyUserType()
      )).subscribe();
    }
  }

  sendCodeAgain() {
    this.showForm = !this.showForm;
    this.verifySubscription = this.verifyService.sendCodeAgain(this.verifyId).subscribe();
  }

  ngOnDestroy(): void {
    this.verifySubscription.unsubscribe;
  }

}
