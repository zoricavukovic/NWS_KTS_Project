import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';
import { DrivingNotificationRequest } from 'src/app/model/request/driving-notification-request';
import { PossibleRoute } from 'src/app/model/route/possible-routes';
import { User } from 'src/app/model/user/user';
import { VehicleTypeInfo } from 'src/app/model/vehicle/vehicle-type-info';
import { AuthService } from 'src/app/service/auth.service';
import { DrivingNotificationService } from 'src/app/service/driving-notification.service';
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
  vehicleTypesSeats = {};
  price = 0;
  currentUser: User = null;

  allRegularUsers: string[] = [];
  selectedPassengers: string[] = [];

  passengerCtrl: FormControl = new FormControl();

  private allUsersSubscription: Subscription;
  private priceSubscription: Subscription;
  private authSubscription: Subscription;
  private drivingNotificationSubscription: Subscription;
  private vehicleTypesSubscription: Subscription;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private vehicleService: VehicleService,
    private drivingNotificationService: DrivingNotificationService,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.currentUser = user;
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
      this.allRegularUsers.push(passenger);
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
      /*const first_route = this.route.pointList.at(0);
      const end_route = this.route.pointList.at(
        this.route.pointList.length - 1
        );*/
      const first_route = [45.262402102988666, 19.83108921294311];
      const end_route = [45.2431212554299, 19.820428580417126];
      const drivingNotification = {
        lonStarted: first_route[0],
        latStarted: first_route[1],
        lonEnd: end_route[0],
        latEnd: end_route[1],
        senderEmail: this.currentUser.email,
        price: this.price,
        passengers: this.selectedPassengers,
        started: new Date(),
        duration: 5,
        petFriendly: this.petFriendly,
        babySeat: this.babySeat,
        vehicleType: this.vehicleType,
      };

      console.log(drivingNotification);
      this.drivingNotificationSubscription = this.drivingNotificationService
        .saveDrivingNotification(drivingNotification)
        .subscribe(response => {
          console.log('usppelo');
        });
    }
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
