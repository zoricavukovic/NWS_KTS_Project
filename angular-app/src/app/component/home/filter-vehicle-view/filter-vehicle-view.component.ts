import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subscription } from 'rxjs';
import { PossibleRoute } from 'src/app/model/response/possible-routes';
import { UserService } from 'src/app/service/user.service';
import { VehicleService } from 'src/app/service/vehicle.service';

@Component({
  selector: 'app-filter-vehicle-view',
  templateUrl: './filter-vehicle-view.component.html',
  styleUrls: ['./filter-vehicle-view.component.css'],
})
export class FilterVehicleViewComponent implements OnInit, OnDestroy {
  @Input() route: PossibleRoute;

  petFriendly = false;
  babySeat = false;
  vehicleType: string;
  price = 0;

  allRegularUsers = new Array<string>();
  selectedPassengers = new Array<string>();

  passengerCtrl = new FormControl();

  private allUsersSubscription: Subscription;
  private priceSubscription: Subscription;

  constructor(
    private userService: UserService,
    private vehicleService: VehicleService
  ) {}

  ngOnInit(): void {
    this.allUsersSubscription = this.userService
      .getAllRegularUsers()
      .subscribe(users => {
        for (const user of users) {
          this.allRegularUsers.push(user.email);
        }
      });
  }

  ngOnDestroy(): void {
    if (this.allUsersSubscription) {
      this.allUsersSubscription.unsubscribe();
    }
    if (this.priceSubscription) {
      this.priceSubscription.unsubscribe();
    }
  }

  addSelectedPassenger(email: string) {
    this.selectedPassengers.push(email);
    this.passengerCtrl.setValue(null);
  }

  removePassengerFromSelected(passenger: string): void {
    const index = this.selectedPassengers.indexOf(passenger);

    if (index >= 0) {
      this.selectedPassengers.splice(index, 1);
    }
  }

  setVehicleTypeAndShowPrice(vehicleType: string) {
    this.vehicleType = vehicleType;
    console.log(this.vehicleType);
    this.priceSubscription = this.vehicleService
      .getPriceForVehicleAndRoute(this.vehicleType, 3800)
      .subscribe(response => {
        this.price = response;
      });
  }

  findDriver() {
    if (this.selectedPassengers.length === 0) {
      console.log('nisuu, trazi vozaca');
    } else {
      console.log('saljii voznju');
    }
  }
}
