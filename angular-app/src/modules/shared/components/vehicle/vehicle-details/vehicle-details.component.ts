import { Component, Input } from '@angular/core';
import {Vehicle} from "../../../models/vehicle/vehicle";
import {VehicleTypeInfo} from "../../../models/vehicle/vehicle-type-info";

@Component({
  selector: 'vehicle-details',
  templateUrl: './vehicle-details.component.html',
  styleUrls: ['./vehicle-details.component.css'],
})
export class VehicleDetailsComponent {
  @Input() vehicle: Vehicle;

  vehicle_image = {
    VAN: '/assets/images/van.png',
    SUV: '/assets/images/suv.png',
    CAR: '/assets/images/car.png',
  };
}
