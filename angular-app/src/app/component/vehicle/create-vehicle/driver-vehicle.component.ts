import {
  Component,
  EventEmitter,
  OnDestroy,
  OnInit,
  Output,
} from '@angular/core';
import { Subscription } from 'rxjs';
import { VehicleTypeInfo } from 'src/app/model/vehicle/vehicle-type-info';
import { VehicleTypeInfoService } from 'src/app/service/vehicle-type-info.service';
import { VehicleService } from 'src/app/service/vehicle.service';

@Component({
  selector: 'driver-vehicle',
  templateUrl: './driver-vehicle.component.html',
  styleUrls: ['./driver-vehicle.component.css'],
})
export class DriverVehicleComponent implements OnInit, OnDestroy {
  @Output()
  petFriendlyEvent = new EventEmitter<boolean>();

  @Output()
  babySeatEvent = new EventEmitter<boolean>();

  @Output()
  vehicleTypeEvent = new EventEmitter<string>();

  petFriendly = false;
  babySeat = false;
  selectedVehicleType: string;
  vehicleTypes: VehicleTypeInfo[];
  vehicleTypesSubscription: Subscription;
  styleArray = [];
  responsiveOptions;
  index = 0;

  constructor(
    private vehicleService: VehicleService,
    private vehicleTypeInfoService: VehicleTypeInfoService
  ) {}

  ngOnInit(): void {
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

  firePetFriendlyEvent() {
    this.petFriendly = !this.petFriendly;
    this.petFriendlyEvent.emit(this.petFriendly);
  }

  fireBabySeatEvent() {
    this.babySeat = !this.babySeat;
    this.babySeatEvent.emit(this.babySeat);
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

    this.vehicleTypeEvent.emit(this.selectedVehicleType);
  }

  ngOnDestroy(): void {
    this.vehicleTypesSubscription.unsubscribe();
  }
}
