import {Component, Input, OnInit, OnDestroy, Output, EventEmitter} from '@angular/core';
import { ControlContainer, FormControl, FormGroup } from '@angular/forms';
import { Select, Store } from '@ngxs/store';
import { ToastrService } from 'ngx-toastr';
import { map, Observable, startWith, Subscription } from 'rxjs';
import {Vehicle} from "../../models/vehicle/vehicle";
import {DrivingNotificationState} from "../../state/driving-notification.state";
import {User} from "../../models/user/user";
import {AddDrivingNotification, UpdateStatusDrivingNotification} from "../../actions/driving-notification.action";
import {UserService} from "../../services/user-service/user.service";
import {RegularUserService} from "../../services/regular-user-service/regular-user.service";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {DrivingNotification} from "../../models/notification/driving-notification";
import {VehicleService} from "../../services/vehicle-service/vehicle.service";
import {VehicleTypeInfoService} from "../../services/vehicle-type-info-service/vehicle-type-info.service";
import {
  DrivingNotificationService
} from "../../services/driving-notification-service/driving-notification.service";
import {Route} from "../../models/route/route";
import { DrivingService } from 'src/modules/shared/services/driving-service/driving.service';
import moment from 'moment';

@Component({
  selector: 'app-filter-vehicle-view',
  templateUrl: './filter-vehicle-view.component.html',
  styleUrls: ['./filter-vehicle-view.component.css'],
})
export class FilterVehicleViewComponent implements OnInit, OnDestroy {
  @Select(DrivingNotificationState.getDrivingNotification)drivingNotification$: Observable<DrivingNotification>;
  @Input() requestLater: boolean;
  @Output() waitingForAcceptDrive = new  EventEmitter<boolean>();
  @Output() enterLocationsViewEvent = new EventEmitter<boolean>();
  vehiclePassengersView = true;
  vehicle: Vehicle;

  petFriendly = false;
  usersHaveAlreadyRide = false;
  babySeat = false;
  vehicleType: string;
  vehicleTypesSeats = {};
  price = 0;
  currentUser: User = null;

  allRegularUsers: string[] = [];
  filteredRegularUsers: Observable<string[]>;
  selectedPassengers: string[];
  passengers: User[] = [];

  passengerCtrl: FormControl = new FormControl();

  private allUsersSubscription: Subscription;
  private priceSubscription: Subscription;
  private authSubscription: Subscription;
  private drivingNotificationSubscription: Subscription;
  private passengerSubscription: Subscription;
  private vehicleTypesSubscription: Subscription;
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
    private store: Store,
    private drivingService: DrivingService
  ) {
    this.rideRequestForm = <FormGroup>this.controlContainer.control;
    this.selectedPassengers = this.rideRequestForm.get('selectedPassengers').value;

  }

  ngOnInit(): void {
  
    this.authSubscription = this.authService
      .getSubjectCurrentUser()
      .subscribe(user => {
        this.currentUser = user;
        this.rideRequestForm.get('senderEmail').setValue(this.currentUser.email);
        this.passengers.push(user);
      });

    this.allUsersSubscription = this.regularUserService
      .getAll()
      .subscribe(regularUsersResponse => {
        for (const user of regularUsersResponse) {
          console.log(user.email);
          if(!this.rideRequestForm.get('selectedPassengers').value.includes(user.email) 
          && user.email !== this.currentUser.email){
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

  goToEnterLocations(){
    this.enterLocationsViewEvent.emit(true);
  }

  addSelectedPassenger(email: string) {
    if (this.rideRequestForm.get('vehicleType').value) {
      if (
        this.vehicleTypesSeats[this.rideRequestForm.get('vehicleType').value] >
        this.selectedPassengers.length + 1
      ) {
        this.selectedPassengers.push(email);
        this.rideRequestForm.get('selectedPassengers').setValue(this.selectedPassengers);
        this._findPassengerObj(email);
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
      this.rideRequestForm.get('selectedPassengers').setValue(this.selectedPassengers);
      this.passengers.splice(index, 1);
      this.allRegularUsers.push(passenger);
    }
  }

  showPrice(){
    const distance = (this.rideRequestForm.get('selectedRoute').value as Route).distance;
    this.priceSubscription = this.vehicleTypeInfoService
      .getPriceForVehicleAndRoute(this.rideRequestForm.get('vehicleType').value, distance)
      .subscribe(response => {
        this.price = response;
        this.rideRequestForm.get('price').setValue(this.price);
      });
  }

  checkChosenDateTime(): boolean{
    if(this.rideRequestForm.get('chosenDateTime').value > new Date(Date.now() + (5*60*60*1000)) || this.rideRequestForm.get('chosenDateTime').value < new Date(Date.now() + (0.5*60*60*1000))){
      this.toast.error('You can only schedule your ride 30 minutes to 5 hours in advance!', 'Invalid chosen time');
      return false;
    }
    return true;
  }

  requestRide() {
    this.rideRequestForm.get('selectedPassengers').setValue(this.selectedPassengers);
    let started = moment().toDate();
    if(this.rideRequestForm.get('chosenDateTime').value !== null){
      started = this.rideRequestForm.get('chosenDateTime').value
    }
    if(this.rideRequestForm.get('chosenDateTime').value !== null && !this.checkChosenDateTime()){
      return;
    }
    const timeZone = new Intl.DateTimeFormat().resolvedOptions().timeZone;
    console.log(timeZone);
    this.passengerSubscription = this.drivingService.havePassengersAlreadyRide(this.selectedPassengers, started).subscribe(response => {
      this.usersHaveAlreadyRide = response;
      if(response){
        this.toast.error("Some passengers have scheduled ride at time.", "Ride request failed");
      }
      else{
       this.createDriving(started);
      }
    });
  }

  private createDriving(started: Date){
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
        chosenDateTime: started,
        reservation: this.rideRequestForm.get('chosenDateTime').value != null
      };
      this.store.dispatch(new AddDrivingNotification(drivingNotification)).subscribe((response) => {
        console.log(response);
      });

      this.drivingNotificationSubscription = this.drivingNotificationService.create(drivingNotification).subscribe((result) => {
        this.store.dispatch(new UpdateStatusDrivingNotification({active: false, drivingStatus: "ACCEPTED"}))
          .subscribe(response => {
          console.log(response);
        })
    },
        error => {
          this.toast.error(error.error, "Requesting ride failed")
        }
    )
  }

  private _findPassengerObj(email: string): void {
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
