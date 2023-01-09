import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { PayingInfo } from 'src/app/model/payment/paying-info';
import { PayingInfoService } from 'src/app/service/paying-info.service';
import { environment } from 'src/environments/environment';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-paying-info-box',
  templateUrl: './paying-info-box.component.html',
  styleUrls: ['./paying-info-box.component.css']
})
export class PayingInfoBoxComponent implements OnInit, OnDestroy {

  @Input() payingInfo: PayingInfo;

  change: boolean = false;
  payingInfoSubscription: Subscription;

  payingInfoForm = new FormGroup(
    {
      price: new FormControl('', [
        Validators.required,
        Validators.pattern('[0-9][.]?[0-9]*')
      ]),
      maxNumOfTokensPerTransaction: new FormControl('', [
        Validators.required,
        Validators.min(1)
      ])
  });

  constructor(
    private _payingInfosService: PayingInfoService,
    private _toast: ToastrService,
  ) { }

  
  ngOnInit(): void {
  }
  
  updatePayingInfos() {
    let payingInfoUpdated: PayingInfo = this._payingInfosService.createPayingInfoObj(+this.payingInfoForm.get('price').value, +this.payingInfoForm.get('maxNumOfTokensPerTransaction').value);
    console.log(payingInfoUpdated)
    this.payingInfoSubscription = this._payingInfosService.update(payingInfoUpdated).subscribe(
      res => {
        this.payingInfo = res;
        this.payingInfoForm.get('price').setValue('');
        this.payingInfoForm.get('maxNumOfTokensPerTransaction').setValue('');
        this.payingInfoForm.markAsUntouched();
        this._toast.success(
          'Paying info is updated successfully',
          'Success!'
        );
      },
      err => {
        this._toast.error(
          'You cannot update paying info, check if data is valid.',
          'Paying info update failed.'
        );
      }
    );
  }

  ngOnDestroy(): void {
    if (this.payingInfoSubscription) {
      this.payingInfoSubscription.unsubscribe();
    }
  }

}
