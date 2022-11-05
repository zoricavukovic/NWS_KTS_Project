import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { VehicleTypeInfo } from 'src/app/model/vehicle-type-info-response';
import { VehicleService } from 'src/app/service/vehicle.service';

@Component({
  selector: 'driver-vehicle',
  templateUrl: './driver-vehicle.component.html',
  styleUrls: ['./driver-vehicle.component.css']
})
export class DriverVehicleComponent implements OnInit, OnDestroy {

  petFriendly: boolean = false;
  babySeat: boolean = false;
  selectedVehicleType: string;
  vehicleTypes: VehicleTypeInfo[];
  vehicleTypesSubscription: Subscription;
  styleArray = [];
  responsiveOptions;
  index = 0;

  constructor(private vehicleService: VehicleService) { }

  ngOnInit(): void {
    this.vehicleTypesSubscription = this.vehicleService.getVehicleTypeInfos()
      .subscribe(
        vehicleTypes => {
          vehicleTypes.forEach((vehicleType, index)=> 
            vehicleType = this.setAdditionalData(vehicleType, index))
          this.vehicleTypes = vehicleTypes;
          this.styleArray = new Array<boolean>(vehicleTypes.length).fill(false);
       }   
    );

    this.responsiveOptions = [
      {
          breakpoint: '1024px',
          numVisible: 3,
          numScroll: 3
      },
      {
          breakpoint: '768px',
          numVisible: 2,
          numScroll: 2
      },
      {
          breakpoint: '560px',
          numVisible: 1,
          numScroll: 1
      }
    ];
  }
  setAdditionalData(vehicleType: VehicleTypeInfo, index: number): VehicleTypeInfo {
    vehicleType.index = index;
    vehicleType.img = this.getPhoto(vehicleType);
    return vehicleType;
  }

  getPhoto(vehicleType: VehicleTypeInfo): string {
    switch (vehicleType.vehicleType){
      case 'VAN':

        return "/van.png";
      case 'SUV':

        return  "/suv.png";
      default:

        return  "/car.png";
    }
  }

  changeSelectedVehicleType(selectedVehicleType) {
    this.selectedVehicleType = selectedVehicleType;
    this.styleArray.forEach((value, index)=> 
            this.styleArray[index] = selectedVehicleType.index === index
    );
  }

  ngOnDestroy(): void {
    this.vehicleTypesSubscription.unsubscribe();
  }

}