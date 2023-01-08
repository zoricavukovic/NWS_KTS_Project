import { Component, Input, OnInit } from '@angular/core';
import { DrivingLocation } from 'src/app/model/route/driving-location';
import { User } from 'src/app/model/user/user';
import { Vehicle } from 'src/app/model/vehicle/vehicle';

@Component({
  selector: 'app-accepting-driving-view',
  templateUrl: './accepting-driving-view.component.html',
  styleUrls: ['./accepting-driving-view.component.css'],
})
export class AcceptingDrivingViewComponent implements OnInit {
  @Input() locations: DrivingLocation[];
  @Input() price: number;
  @Input() passengers: User[];
  @Input() vehicle: Vehicle;

  ngOnInit(): void {
    console.log(this.passengers);
    console.log("Doslo je ovde");
  }
}
