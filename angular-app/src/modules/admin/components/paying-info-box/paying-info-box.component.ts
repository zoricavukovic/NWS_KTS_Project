import { Component, Input, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import {PayingInfo} from "../../../shared/models/payment/paying-info";
import {PayingInfoService} from "../../services/paying-info-service/paying-info.service";

@Component({
  selector: 'app-paying-info-box',
  templateUrl: './paying-info-box.component.html',
  styleUrls: ['./paying-info-box.component.css']
})
export class PayingInfoBoxComponent implements OnDestroy {

  @Input() payingInfo: PayingInfo;

  change = false;
  payingInfoSubscription: Subscription;

  payingInfoForm = new FormGroup(
    {
      price: new FormControl('', [
        Validators.required,
        Validators.pattern('[0-9][.]?[0-9]*')
      ]),
      maxNumOfTokensPerTransaction: new FormControl('', [
        Validators.required,
        Validators.pattern('[1-9][0-9]*'),
        Validators.min(1)
      ])
  });

  constructor(
    private _payingInfosService: PayingInfoService,
    private _toast: ToastrService,
  ) { }

  updatePayingInfos() {
    const payingInfoUpdated: PayingInfo = this._payingInfosService.createPayingInfoObj(+this.payingInfoForm.get('price').value, +this.payingInfoForm.get('maxNumOfTokensPerTransaction').value);
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
        console.log(err);
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
