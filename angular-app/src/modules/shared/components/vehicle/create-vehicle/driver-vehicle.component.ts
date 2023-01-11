import {
  Component,
  EventEmitter,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { ControlContainer, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import {VehicleTypeInfo} from "../../../models/vehicle/vehicle-type-info";
import {VehicleService} from "../../../services/vehicle-service/vehicle.service";
import {VehicleTypeInfoService} from "../../../services/vehicle-type-info-service/vehicle-type-info.service";

@Component({
  selector: 'driver-vehicle',
  templateUrl: './driver-vehicle.component.html',
  styleUrls: ['./driver-vehicle.component.css'],
})
export class DriverVehicleComponent implements OnInit, OnDestroy {
  @Output() vehicleTypeEvent = new EventEmitter();
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
  }

  ngOnInit(): void {
    this.rideRequestForm = <FormGroup>this.controlContainer.control;
    console.log(this.rideRequestForm);
    this.vehicleTypesSubscription = this.vehicleTypeInfoService
      .getAll()
      .subscribe(vehicleTypes => {
        vehicleTypes.forEach(
          (vehicleType, index) =>
            (vehicleType = this.setAdditionalData(vehicleType, index))
        );
        this.vehicleTypes = vehicleTypes;
        this.styleArray = new Array<boolean>(vehicleTypes.length).fill(false);
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


  changeSelectedVehicleType(selectedVehicleType) {
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
