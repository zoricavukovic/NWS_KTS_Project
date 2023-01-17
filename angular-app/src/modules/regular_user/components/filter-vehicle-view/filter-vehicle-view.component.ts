import {Component, Input, OnInit, OnDestroy, Output, EventEmitter} from '@angular/core';
import { ControlContainer, FormControl, FormGroup } from '@angular/forms';
import { Select, Store } from '@ngxs/store';
import { ToastrService } from 'ngx-toastr';
import { map, Observable, startWith, Subscription } from 'rxjs';
import {Vehicle} from "../../../shared/models/vehicle/vehicle";
import {DrivingNotificationState} from "../../../shared/state/driving-notification.state";
import {User} from "../../../shared/models/user/user";
import {AddDrivingNotification, UpdateStatusDrivingNotification} from "../../../shared/actions/driving-notification.action";
import {UserService} from "../../../shared/services/user-service/user.service";
import {RegularUserService} from "../../../shared/services/regular-user-service/regular-user.service";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {DrivingNotification} from "../../../shared/models/notification/driving-notification";
import {VehicleService} from "../../../shared/services/vehicle-service/vehicle.service";
import {VehicleTypeInfoService} from "../../../shared/services/vehicle-type-info-service/vehicle-type-info.service";
import {
  DrivingNotificationService
} from "../../../shared/services/driving-notification-service/driving-notification.service";
import {Route} from "../../../shared/models/route/route";

@Component({
  selector: 'app-filter-vehicle-view',
  templateUrl: './filter-vehicle-view.component.html',
  styleUrls: ['./filter-vehicle-view.component.css'],
})
export class FilterVehicleViewComponent implements OnInit, OnDestroy {
  @Select(DrivingNotificationState.getDrivingNotification)drivingNotification$: Observable<DrivingNotification>;
  @Input() requestLater: boolean;
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
  filteredRegularUsers: Observable<string[]>;
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
    this.rideRequestForm = <FormGroup>this.controlContainer.control;

  }

  ngOnInit(): void {
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
        this.filteredRegularUsers = this.passengerCtrl.valueChanges.pipe(startWith(''),
        map(value => this._filter(value || '')),
      );
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
    // this.vehicleSubscription = this.vehicleService
    //   .getVehicleByVehicleType(this.rideRequestForm.get('vehicleType').value)
    //   .subscribe(response => {
    //     this.vehicle = response;
    //   });
    this.priceSubscription = this.vehicleTypeInfoService
      .getPriceForVehicleAndRoute(this.rideRequestForm.get('vehicleType').value, 3800)
      .subscribe(response => {
        this.price = response;
        this.rideRequestForm.get('price').setValue(this.price);
      });
    }

  checkChosenDateTime(): boolean{
    if(this.rideRequestForm.get('chosenDateTime').value > new Date(Date.now() + (5*60*60*1000))){
      this.toast.error('You can only schedule your ride 5 hours in advance!', 'Invalid chosen time');
      return false;
    }
    return true;
  }

  requestRide() {
    this.rideRequestForm.get('selectedPassengers').setValue(this.selectedPassengers);
    this.rideRequestForm.get('senderEmail').setValue(this.currentUser.email);

    if(this.checkChosenDateTime()){
      this.waitingForAcceptDrive.emit(true);
      const drivingNotification = {
        route: this.rideRequestForm.get('selectedRoute').value,
        price: this.rideRequestForm.get('price').value,
        senderEmail: this.rideRequestForm.get('senderEmail').value,
        passengers: this.rideRequestForm.get('selectedPassengers').value,
        duration: 5,
        petFriendly: this.rideRequestForm.get('petFriendly').value,
        babySeat: this.rideRequestForm.get('babySeat').value,
        vehicleType: this.rideRequestForm.get('vehicleType').value,
        minutes: -1,
        drivingStatus: "PAYING",
        active: false,
        chosenDateTime: this.rideRequestForm.get('chosenDateTime').value
      };
      console.log(drivingNotification);
      this.store.dispatch(new AddDrivingNotification(drivingNotification)).subscribe((response) => {
        console.log(response);
      });

      this.drivingNotificationSubscription = this.drivingNotificationService.create(drivingNotification).subscribe((result) => {
        this.store.dispatch(new UpdateStatusDrivingNotification({active: false, drivingStatus: "ACCEPTED"})).subscribe(response => {
          console.log(response);
        })
      },

      (error) => {
        console.log(error);
        this.toast.error(error.error, 'Create ride failed.');
      });
  }

    // this.drivingNotificationSubscription = this.store
    // .dispatch(new AddDrivingNotification(drivingNotification))
    // .subscribe((response) => {
    //   console.log(response);
    //   console.log("lfddsf");
    // });
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

  

private _filter(value: string): string[] {
  const filterValue = value.toLowerCase();

  return this.allRegularUsers.filter(option => option.toLowerCase().includes(filterValue));
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
