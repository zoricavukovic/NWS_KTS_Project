import { DATE_PIPE_DEFAULT_TIMEZONE } from '@angular/common';
import { ThisReceiver } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { ControlContainer, FormGroup } from '@angular/forms';
import moment from 'moment';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-request-later-time',
  templateUrl: './request-later-time.component.html',
  styleUrls: ['./request-later-time.component.css']
})
export class RequestLaterTimeComponent implements OnInit {
  chosenTime: moment.Moment;
  rideRequestForm: FormGroup;


  constructor(private toast: ToastrService, private controlContainer: ControlContainer) { 
    this.rideRequestForm = <FormGroup>this.controlContainer.control;
  }

  ngOnInit(): void {
    this.rideRequestForm = <FormGroup>this.controlContainer.control;
    this.chosenTime = moment().add(1, 'hour');
  }


  getChosenDateTime(): Date {
    const currentDateTime = moment();
    const currentTime = moment(`${currentDateTime.hour()}:${currentDateTime.minutes()}`, "HH:mm");
     console.log(`${currentDateTime.hour()}:${currentDateTime.minutes()}`);
    console.log(currentTime);
    const time = moment(`${this.chosenTime.hour()}:${this.chosenTime.minutes()}`, "HH:mm");
    if(currentTime.isAfter(time)){
      // moment([this.chosenTime.hours, this.chosenTime.minutes])
      this.chosenTime = this.chosenTime.add(1, 'day')
      console.log(this.chosenTime);
    }
    else{
      if(this.chosenTime.isAfter(currentDateTime, 'day')){
        this.chosenTime = this.chosenTime.subtract(1, 'day');
      }
    }
    this.rideRequestForm.get('chosenDateTime').setValue(this.chosenTime.toDate());
    return this.chosenTime.toDate();
  }

}
