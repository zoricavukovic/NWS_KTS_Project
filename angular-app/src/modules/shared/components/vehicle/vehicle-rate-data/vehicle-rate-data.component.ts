import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { Vehicle } from 'src/modules/shared/models/vehicle/vehicle';
import { VehicleService } from 'src/modules/shared/services/vehicle-service/vehicle.service';

@Component({
  selector: 'app-vehicle-rate-data',
  templateUrl: './vehicle-rate-data.component.html',
  styleUrls: ['./vehicle-rate-data.component.css']
})
export class VehicleRateDataComponent implements OnInit, OnDestroy {

  @Input() driverId: number;

  vehicleSubscription: Subscription;
  vehicle: Vehicle;

  constructor(
    private vehicleService: VehicleService
  ) { }

  ngOnInit(): void {
    if (this.driverId) {
      this.vehicleSubscription = this.vehicleService.getVehicleByDriver(this.driverId.toString()).subscribe(
        res => {
          this.vehicle = res;
        }
      );
    }
  }

  ngOnDestroy(): void {
    if (this.vehicleSubscription) {
      this.vehicleSubscription.unsubscribe();
    }
  }

}
