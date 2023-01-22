import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { map, Observable, startWith, Subscription } from 'rxjs';
import { Router } from '@angular/router';
import {ToastrService} from 'ngx-toastr';
import {User} from "../../../shared/models/user/user";
import {ConfigService} from "../../../shared/services/config-service/config.service";
import {UserService} from "../../../shared/services/user-service/user.service";
import {AuthService} from "../../../auth/services/auth-service/auth.service";
import {MyErrorStateMatcher} from "../registration/registration.component";
import { VehicleService } from 'src/modules/shared/services/vehicle-service/vehicle.service';
import { Vehicle } from 'src/modules/shared/models/vehicle/vehicle';

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css'],
})
export class EditProfileComponent implements OnInit, OnDestroy {
  user: User;

  authSubscription: Subscription;
  updateProfileSubscription: Subscription;
  vehicleSubscription: Subscription;
  isDriver: boolean;
  vehicle: Vehicle;

  editDataForm = new FormGroup({
    phoneNumberFormControl: new FormControl('', [
      Validators.required,
      Validators.pattern('[0-9]{8,12}'),
    ]),
    nameFormControl: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z ]*'),
    ]),
    surnameFormControl: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z ]*'),
    ]),
    cityFormControl: new FormControl('', [
      Validators.required,
      Validators.pattern('[a-zA-Z ]*'),
    ]),
    petFriendly: new FormControl(false, []),
    babySeat: new FormControl(false, []),
    vehicleType: new FormControl(null, [])
  });

  matcher = new MyErrorStateMatcher();
  cities: string[];
  filteredCities: Observable<string[]>;

  constructor(
    public authService: AuthService,
    public configService: ConfigService,
    private userService: UserService,
    private router: Router,
    private toast: ToastrService,
    private vehicleService: VehicleService
  ) {
    this.filteredCities = this.editDataForm
      .get('cityFormControl')
      .valueChanges.pipe(
        startWith(''),
        map(city => (city ? this._filterCities(city) : this.cities.slice()))
      );

    this.user = null;
    this.isDriver = false;
    this.vehicle = null;
    this.cities = ['Beograd', 'Novi Sad', 'Kraljevo', 'Kragujevac', 'Jagodina', 'Mladenovac', 'Subotica', 'Ruma', 'Priboj', 'Sabac', 'Leskovac', 'Vranje', 'Smederevo', 'Pozarevac', 'Zrenjanin', 'Sombor'];
  }

  ngOnInit(): void {
    this.authSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        if (user) {
          this.user = user
          this.isDriver = this.authService.userIsDriver();
          this.editDataForm.get('nameFormControl').setValue(this.user.name),
          this.editDataForm.get('surnameFormControl').setValue(this.user.surname),
          this.editDataForm.get('phoneNumberFormControl').setValue(this.user.phoneNumber),
          this.editDataForm.get('cityFormControl').setValue(this.user.city),
          this.loadVehicle();
        }
      }
    );
  }

  checkFormRequirements() {
    if (this.isDriver && this.editDataForm && this.vehicle) {
      this.editDataForm.get('petFriendly').setValidators(Validators.required);
      this.editDataForm.get('petFriendly').setValue(this.vehicle.petFriendly);
      this.editDataForm.get('babySeat').setValidators(Validators.required);
      this.editDataForm.get('babySeat').setValue(this.vehicle.babySeat);
      this.editDataForm.get('vehicleType').setValidators(Validators.required);
      this.editDataForm.get('vehicleType').setValue(this.vehicle.vehicleType);
      this.editDataForm.updateValueAndValidity();
    }
  }

  loadVehicle(): void {
    if (this.isDriver) {
      this.vehicleSubscription = this.vehicleService.getVehicleByDriver(this.user.id.toString()).subscribe(
        res => {
          this.vehicle = res;
          this.checkFormRequirements();
        }
      );
    }
  }

  saveChanges() {
    this.updateProfileSubscription = this.userService
      .updateProfileData(
        this.userService.createUserDetails(
          this.user.email,
          this.editDataForm.get('nameFormControl').value,
          this.editDataForm.get('surnameFormControl').value,
          this.editDataForm.get('phoneNumberFormControl').value,
          this.editDataForm.get('cityFormControl').value,
          this.user.role,
          this.editDataForm.get('petFriendly').value,
          this.editDataForm.get('babySeat').value,
          this.editDataForm.get('vehicleType').value
        )
      )
      .subscribe(
        res => {
          const parsedUser = res as User;
          this.authService.setUserInLocalStorage(parsedUser);
          this.router.navigate([`/serb-uber/user/user-profile/${this.user.id}`]);
          this.authService.userIsDriver()
            ? this.toast.success(
                'Update request is sent to admin!',
                'Update request completed'
              )
            : this.toast.success(
                'Profile is updated successfully!',
                'Profile update completed'
              );
        },
        error => this.toast.error(error.error, 'Profile update failed')
      );
  }

  cancel() {
    this.router.navigate([`/serb-uber/user/user-profile/${this.user.id}`]);
  }

  checkValidForm():boolean {
    return this.editDataForm.invalid;
  }

  _filterCities(value: string): string[] {
    const filterValue = value.toLowerCase();

    return this.cities.filter(city => city.toLowerCase().includes(filterValue));
  }

  ngOnDestroy(): void {
    if (this.authSubscription) {
      this.authSubscription.unsubscribe();
    }

    if (this.updateProfileSubscription) {
      this.updateProfileSubscription.unsubscribe();
    }

    if (this.vehicleSubscription) {
      this.vehicleSubscription.unsubscribe();
    }
  }
}
