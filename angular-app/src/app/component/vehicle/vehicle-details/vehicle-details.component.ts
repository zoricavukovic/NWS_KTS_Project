import {Component, Input, OnInit} from '@angular/core';
import {Vehicle} from "../../../model/response/vehicle";

@Component({
  selector: 'vehicle-details',
  templateUrl: './vehicle-details.component.html',
  styleUrls: ['./vehicle-details.component.css']
})
export class VehicleDetailsComponent implements OnInit {

  @Input() vehicle: Vehicle;
  vehicle_image = {
    "VAN": '/assets/images/van.png',
    "SUV": '/assets/images/suv.png',
    "CAR": '/assets/images/car.png'
  };

  constructor() { }

  ngOnInit(): void {
  }

}
