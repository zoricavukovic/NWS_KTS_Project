import {Component, Input, OnInit, OnDestroy, Output, EventEmitter} from '@angular/core';
import { ControlContainer, FormControl, FormGroup } from '@angular/forms';
import { Select, Store } from '@ngxs/store';
import { ToastrService } from 'ngx-toastr';
import { Observable, Subscription } from 'rxjs';
import { AddDrivingNotification } from 'src/app/actions/driving-notification.action';
import { DrivingNotification } from 'src/app/model/notification/driving-notification';
import { User } from 'src/app/model/user/user';
import { Vehicle } from 'src/app/model/vehicle/vehicle';
import { AuthService } from 'src/app/service/auth.service';
import { DrivingNotificationService } from 'src/app/service/driving-notification.service';
import { RegularUserService } from 'src/app/service/regular-user.service';
import { UserService } from 'src/app/service/user.service';
import { VehicleTypeInfoService } from 'src/app/service/vehicle-type-info.service';
import { VehicleService } from 'src/app/service/vehicle.service';
import { DrivingNotificationState } from 'src/app/state/driving-notification.state';
import { Route } from '../../../model/route/route';

@Component({
  selector: 'app-filter-vehicle-view',
  templateUrl: './filter-vehicle-view.component.html',
  styleUrls: ['./filter-vehicle-view.component.css'],
})
export class FilterVehicleViewComponent implements OnInit, OnDestroy {
  @Select(DrivingNotificationState.getDrivingNotification)drivingNotification$: Observable<DrivingNotification>;
  @Input() route: Route;
  @Output() waitingForAcceptDrive = new  EventEmitter<boolean>();
  vehiclePassengersView = true;
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
  rideRequestForm: FormGroup;

  constructor(
    private userService: UserService,
    private regularUserService: RegularUserService,
    private authService: AuthService,
    private vehicleService: VehicleService,
    private drivingNotificationService: DrivingNotificationService,
    private vehicleTypeInfoService: VehicleTypeInfoService,
    private toast: ToastrService,
    private controlContainer: ControlContainer,
    private store: Store
  ) {
    console.log(this.route);
    this.rideRequestForm = <FormGroup>this.controlContainer.control;
    
  }

  ngOnInit(): void {
    console.log(this.rideRequestForm);
    console.log(this.route);
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.currentUser = user;
        this.passengers.push(user);
      });

    this.allUsersSubscription = this.regularUserService
      .getAll()
      .subscribe(regularUsersResponse => {
        for (const user of regularUsersResponse) {
          if (user.email !== this.currentUser.email) {
            this.allRegularUsers.push(user.email);
          }
        }
      });

    this.vehicleTypesSubscription = this.vehicleTypeInfoService
      .getAll()
      .subscribe(vehicleTypes => {
        for (const type of vehicleTypes) {
          this.vehicleTypesSeats[type.vehicleType] = type.numOfSeats;
        }
      });
  }

  addSelectedPassenger(email: string) {
    if (this.rideRequestForm.get('vehicleType').value) {
      if (
        this.vehicleTypesSeats[this.rideRequestForm.get('vehicleType').value] >
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

  showPrice(){
      console.log(this.rideRequestForm.get('vehicleType').value);
    this.vehicleSubscription = this.vehicleService
      .getVehicleByVehicleType(this.rideRequestForm.get('vehicleType').value)
      .subscribe(response => {
        this.vehicle = response;
        console.log(response);
      });
    this.priceSubscription = this.vehicleService
      .getPriceForVehicleAndRoute(this.rideRequestForm.get('vehicleType').value, 3800)
      .subscribe(response => {
        this.price = response;
        console.log(this.price);
        this.rideRequestForm.get('price').setValue(this.price);
      });
    }
  

  findDriver() {
    this.rideRequestForm.get('selectedPassengers').setValue(this.selectedPassengers);
    this.rideRequestForm.get('senderEmail').setValue(this.currentUser.email);
    console.log(this.rideRequestForm);
    this.waitingForAcceptDrive.emit(true);
    console.log("blaaa"); 
    const drivingNotification = {
      route: this.rideRequestForm.get('selectedRoute').value,
      price: this.rideRequestForm.get('price').value,
      senderEmail: this.rideRequestForm.get('senderEmail').value,
      passengers: this.rideRequestForm.get('selectedPassengers').value,
      duration: 5,
      petFriendly: this.rideRequestForm.get('petFriendly').value,
      babySeat: this.rideRequestForm.get('babySeat').value,
      vehicleType: this.rideRequestForm.get('vehicleType').value,
    };

    console.log("odogovr1");
    console.log(drivingNotification);
    this.drivingNotificationSubscription = this.store
    .dispatch(new AddDrivingNotification(drivingNotification))
    .subscribe((response) => {
      console.log(response);
    });
    // this.drivingNotificationService
    //   .create(drivingNotification)
    //   .subscribe(response => {
    //     console.log("odogovr2");
    //     console.log(response);
    //     console.log("odogovr3");
    //     // this.waitingForAcceptDrive.emit(false);
    //   });
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
