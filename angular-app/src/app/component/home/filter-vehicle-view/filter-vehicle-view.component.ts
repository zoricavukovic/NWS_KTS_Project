import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/user/user';
import { Vehicle } from 'src/app/model/vehicle/vehicle';
import { AuthService } from 'src/app/service/auth.service';
import { DrivingNotificationService } from 'src/app/service/driving-notification.service';
import { UserService } from 'src/app/service/user.service';
import { VehicleService } from 'src/app/service/vehicle.service';
import { Route } from '../../../model/route/route';

@Component({
  selector: 'app-filter-vehicle-view',
  templateUrl: './filter-vehicle-view.component.html',
  styleUrls: ['./filter-vehicle-view.component.css'],
})
export class FilterVehicleViewComponent implements OnInit, OnDestroy {
  @Input() route: Route;

  vehiclePassengersView = true;
  loadingView = false;
  vehicle: Vehicle;

  petFriendly = false;
  babySeat = false;
  vehicleType: string;
  vehicleTypesSeats = {};
  price = 0;
  currentUser: User = null;

  allRegularUsers: string[] = [];
  selectedPassengers: string[] = [];
  passengers: User[] = [];

  passengerCtrl: FormControl = new FormControl();

  private allUsersSubscription: Subscription;
  private priceSubscription: Subscription;
  private authSubscription: Subscription;
  private drivingNotificationSubscription: Subscription;
  private vehicleTypesSubscription: Subscription;
  private vehicleSubscription: Subscription;
  private userSubscription: Subscription;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private vehicleService: VehicleService,
    private drivingNotificationService: DrivingNotificationService,
    private toast: ToastrService
  ) {
    console.log(this.route);
  }

  ngOnInit(): void {
    console.log(this.route);
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.currentUser = user;
        this.passengers.push(user);
      });

    this.allUsersSubscription = this.userService
      .getAllRegularUsers()
      .subscribe(users => {
        for (const user of users) {
          if (user.email !== this.currentUser.email) {
            this.allRegularUsers.push(user.email);
          }
        }
      });

    this.vehicleTypesSubscription = this.vehicleService
      .getVehicleTypeInfos()
      .subscribe(vehicleTypes => {
        for (const type of vehicleTypes) {
          this.vehicleTypesSeats[type.vehicleType] = type.numOfSeats;
        }
      });
  }

  addSelectedPassenger(email: string) {
    if (this.vehicleType) {
      if (
        this.vehicleTypesSeats[this.vehicleType] >
        this.selectedPassengers.length + 1
      ) {
        this.selectedPassengers.push(email);
        this.findPassengerObj(email);
        console.log(this.passengers);
        this.passengerCtrl.setValue(null);
        const index = this.allRegularUsers.indexOf(email);
        this.allRegularUsers.splice(index, 1);
      } else {
        this.passengerCtrl.setValue(null);
        this.toast.error(
          'Number of passengers will be greater than number of seats!',
          'You can not add passenger!'
        );
      }
    } else {
      this.passengerCtrl.setValue(null);
      this.toast.error(
        'You must choose vehicle type before adding passengers! ',
        'You can not add passenger!'
      );
    }
  }

  removePassengerFromSelected(passenger: string): void {
    const index = this.selectedPassengers.indexOf(passenger);

    if (index >= 0) {
      this.selectedPassengers.splice(index, 1);
      this.passengers.splice(index, 1);
      this.allRegularUsers.push(passenger);
    }
  }

  setVehicleTypeAndShowPrice(vehicleType: string) {
    this.vehicleType = vehicleType;
    this.vehicleSubscription = this.vehicleService
      .getVehicleByVehicleType(vehicleType)
      .subscribe(response => {
        this.vehicle = response;
        console.log(response);
      });
    console.log(this.vehicleType);
    this.priceSubscription = this.vehicleService
      .getPriceForVehicleAndRoute(this.vehicleType, 3800)
      .subscribe(response => {
        this.price = response;
      });
  }

  findDriver() {
    const drivingNotification = {
      route: this.route,
      price: this.price,
      senderEmail: this.currentUser.email,
      passengers: this.selectedPassengers,
      started: new Date(),
      duration: 5,
      petFriendly: this.petFriendly,
      babySeat: this.babySeat,
      vehicleType: this.vehicleType,
    };

    this.drivingNotificationService.saveDrivingNotification(
      drivingNotification
    );
    this.vehiclePassengersView = false;
    this.loadingView = true;
  }

  private findPassengerObj(email: string): void {
    let user: User;
    this.userSubscription = this.userService
      .getUserByEmail(email)
      .subscribe((userResponse: User) => {
        user = userResponse;
        this.passengers.push(user);
      });
  }

  ngOnDestroy(): void {
    if (this.allUsersSubscription) {
      this.allUsersSubscription.unsubscribe();
    }

    if (this.priceSubscription) {
      this.priceSubscription.unsubscribe();
    }

    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }

    if (this.drivingNotificationSubscription) {
      this.drivingNotificationSubscription.unsubscribe();
    }
  }
}
