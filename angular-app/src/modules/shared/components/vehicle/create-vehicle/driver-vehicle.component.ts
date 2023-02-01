import {
  Component,
  EventEmitter,
  Input,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { ControlContainer, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { Vehicle } from 'src/modules/shared/models/vehicle/vehicle';
import { VehicleTypeInfo } from '../../../models/vehicle/vehicle-type-info';
import { VehicleService } from '../../../services/vehicle-service/vehicle.service';
import { VehicleTypeInfoService } from '../../../services/vehicle-type-info-service/vehicle-type-info.service';

@Component({
  selector: 'driver-vehicle',
  templateUrl: './driver-vehicle.component.html',
  styleUrls: ['./driver-vehicle.component.css'],
})
export class DriverVehicleComponent implements OnInit, OnDestroy {
  @Output() vehicleTypeEvent = new EventEmitter();

  @Input() vehicle: Vehicle;

  petFriendly = false;
  babySeat = false;
  selectedVehicleType: string;
  vehicleTypes: VehicleTypeInfo[];
  vehicleTypesSubscription: Subscription;
  rideRequestForm: FormGroup;
  styleArray = [];
  responsiveOptions;
  index = 0;

  constructor(
    private vehicleService: VehicleService,
    private vehicleTypeInfoService: VehicleTypeInfoService,
    private controlContainer: ControlContainer
  ) {
    this.rideRequestForm = <FormGroup>this.controlContainer.control;
    this.vehicle = null;
  }

  ngOnInit(): void {
    this.rideRequestForm = <FormGroup>this.controlContainer.control;
    this.vehicleTypesSubscription = this.vehicleTypeInfoService
      .getAll()
      .subscribe(vehicleTypes => {
        vehicleTypes.forEach(
          (vehicleType, index) =>
            (vehicleType = this.setAdditionalData(vehicleType, index))
        );
        this.vehicleTypes = vehicleTypes;
        this.styleArray = new Array<boolean>(vehicleTypes.length).fill(false);
        const selectedIndex = this.getIndexOfSelectedVehicleType();
        this.styleArray.forEach((value, index) => {
          this.styleArray[index] = selectedIndex === index;
        });
      });

    this.responsiveOptions = [
      {
        breakpoint: '1024px',
        numVisible: 3,
        numScroll: 3,
      },
      {
        breakpoint: '768px',
        numVisible: 2,
        numScroll: 2,
      },
      {
        breakpoint: '560px',
        numVisible: 1,
        numScroll: 1,
      },
    ];
  }

  setAdditionalData(
    vehicleType: VehicleTypeInfo,
    index: number
  ): VehicleTypeInfo {
    vehicleType.index = index;
    vehicleType.img = this.getPhoto(vehicleType);
    return vehicleType;
  }
  getIndexOfSelectedVehicleType(): number {
    let vehicleType = this.rideRequestForm.get('vehicleType').value;
    if (this.vehicle !== null) {
      vehicleType = this.vehicle.vehicleTypeInfo.vehicleType;
    }
    switch (vehicleType) {
      case 'VAN':
        return 0;
      case 'SUV':
        return 1;
      case 'CAR':
        return 2;
      default:
        return -1;
    }
  }

  getPhoto(vehicleType: VehicleTypeInfo): string {
    switch (vehicleType.vehicleType) {
      case 'VAN':
        return '/van.png';
      case 'SUV':
        return '/suv.png';
      default:
        return '/car.png';
    }
  }

  changeSelectedVehicleType(selectedVehicleType: VehicleTypeInfo) {
    this.selectedVehicleType = selectedVehicleType.vehicleType;
    this.styleArray.forEach(
      (value, index) =>
        (this.styleArray[index] = selectedVehicleType.index === index)
    );
    this.rideRequestForm.get('vehicleType').setValue(this.selectedVehicleType);
    this.vehicleTypeEvent.emit();
  }

  ngOnDestroy(): void {
    this.vehicleTypesSubscription.unsubscribe();
  }
}
