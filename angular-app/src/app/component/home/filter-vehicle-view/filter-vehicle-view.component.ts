import { Component, OnInit, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { UserService } from 'src/app/service/user.service';

@Component({
  selector: 'app-filter-vehicle-view',
  templateUrl: './filter-vehicle-view.component.html',
  styleUrls: ['./filter-vehicle-view.component.css'],
})
export class FilterVehicleViewComponent implements OnInit, OnDestroy {
  petFriendly = false;
  babySeat = false;
  vehicleType: string;
  price = 0;

  allRegularUsers = new Array<string>();
  selectedPassengers = new Array<string>();

  private allUsersSubscription: Subscription;

  constructor(private userService: UserService) {}

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
  }

  addSelectedPassenger(email: string) {
    this.selectedPassengers.push(email);
  }

  removePassengerFromSelected(passenger: string): void {
    const index = this.selectedPassengers.indexOf(passenger);

    if (index >= 0) {
      this.selectedPassengers.splice(index, 1);
    }
  }

  showPrice() {
    this.price = 1000;
  }
}
