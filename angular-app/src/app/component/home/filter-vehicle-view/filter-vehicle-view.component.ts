import { Component, Input, OnInit, OnDestroy } from '@angular/core';
import { FormControl } from '@angular/forms';
import { Subscription } from 'rxjs';
import { DrivingNotificationRequest } from 'src/app/model/request/driving-notification-request';
import { PossibleRoute } from 'src/app/model/route/possible-routes';
import { User } from 'src/app/model/user/user';
import { AuthService } from 'src/app/service/auth.service';
import { ChatService } from 'src/app/service/chat.service';
import { DrivingNotificationService } from 'src/app/service/driving-notification.service';
import { UserService } from 'src/app/service/user.service';
import { VehicleService } from 'src/app/service/vehicle.service';
import { ToastrService } from 'ngx-toastr';

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
  currentUser: User;

  allRegularUsers = new Array<string>();
  selectedPassengers = new Array<string>();

  passengerCtrl = new FormControl();

  private allUsersSubscription: Subscription;
  private priceSubscription: Subscription;
  private authSubscription: Subscription;
  private drivingNotificationSubscription: Subscription;

  constructor(
    private userService: UserService,
    private toast: ToastrService,
    private authService: AuthService,
    private chatService: ChatService,
    private vehicleService: VehicleService,
    private drivingNotificationService: DrivingNotificationService
  ) {}

  ngOnInit(): void {
    this.currentUser = this.authService.getCurrentUser;

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
      /*const first_route = this.route.pointList.at(0);
      const end_route = this.route.pointList.at(
        this.route.pointList.length - 1
      );*/
      const first_route = [45.262402102988666, 19.83108921294311];
      const end_route = [45.2431212554299, 19.820428580417126];
      console.log(first_route, end_route);
      const drivingNotification = new DrivingNotificationRequest(
        first_route[0],
        first_route[1],
        end_route[0],
        end_route[1],
        this.currentUser.email,
        this.price,
        this.selectedPassengers
      );
      console.log(drivingNotification);
      this.drivingNotificationSubscription = this.drivingNotificationService
        .saveDrivingNotification(drivingNotification)
        .subscribe(response => {
          this.chatService.sendNotification(drivingNotification);
        });
    }
  }
}
