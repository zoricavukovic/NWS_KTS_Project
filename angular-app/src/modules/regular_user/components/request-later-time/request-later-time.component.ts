import { DATE_PIPE_DEFAULT_TIMEZONE } from '@angular/common';
import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { ControlContainer, FormGroup } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-request-later-time',
  templateUrl: './request-later-time.component.html',
  styleUrls: ['./request-later-time.component.css']
})
export class RequestLaterTimeComponent implements OnInit {
  chosenTime: Date;
  rideRequestForm: FormGroup;


  constructor(private toast: ToastrService, private controlContainer: ControlContainer) { 
    this.rideRequestForm = <FormGroup>this.controlContainer.control;
  }

  ngOnInit(): void {
    this.rideRequestForm = <FormGroup>this.controlContainer.control;
    this.chosenTime = new Date();
  }


  getChosenDateTime(): Date {
    const currentDateTime = new Date();
    if(currentDateTime > this.chosenTime){
      this.chosenTime.setDate(this.chosenTime.getDate() + 1);
    }
    else{
      if(this.chosenTime.getDate() > currentDateTime.getDate()){
        this.chosenTime.setDate(this.chosenTime.getDate() - 1);
      }
    }
    this.rideRequestForm.get('chosenDateTime').setValue(this.chosenTime);
    return this.chosenTime;
  }

}
