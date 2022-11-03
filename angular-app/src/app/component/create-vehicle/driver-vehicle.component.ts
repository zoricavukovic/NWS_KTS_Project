import { Component, Input, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { VehicleTypeInfo } from 'src/app/model/vehicle-type-info-response';
import { RegistrationService } from 'src/app/service/registration.service';

@Component({
  selector: 'driver-vehicle',
  templateUrl: './driver-vehicle.component.html',
  styleUrls: ['./driver-vehicle.component.css']
})
export class DriverVehicleComponent implements OnInit {

  petFriendly: boolean = false;
  babySeat: boolean = false;
  selectedVehicleType: string;
  vehicleTypes: Observable<VehicleTypeInfo[]>;

  constructor(private registrationService: RegistrationService) { }

  ngOnInit(): void {
    this.vehicleTypes = this.registrationService.getVehicleTypeInfos();
    console.log(this.vehicleTypes)
  }

  changeSelectedVehicleType(selectedVehicleType) {
    console.log(selectedVehicleType);
    this.selectedVehicleType = selectedVehicleType;
  }

}
