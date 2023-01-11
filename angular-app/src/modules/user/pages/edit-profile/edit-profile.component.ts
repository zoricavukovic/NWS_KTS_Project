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

@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css'],
})
export class EditProfileComponent implements OnInit, OnDestroy {
  user: User;

  authSubscription: Subscription;
  updateProfileSubscription: Subscription;

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
  });

  matcher = new MyErrorStateMatcher();
  cities: string[] = ['Belgrade', 'Novi Sad', 'Kraljevo', 'Sabac'];
  filteredCities: Observable<string[]>;

  constructor(
    public authService: AuthService,
    public configService: ConfigService,
    private userService: UserService,
    private router: Router,
    private toast: ToastrService
  ) {
    this.filteredCities = this.editDataForm
      .get('cityFormControl')
      .valueChanges.pipe(
        startWith(''),
        map(city => (city ? this._filterCities(city) : this.cities.slice()))
      );

    this.user = null;
  }

  ngOnInit(): void {
    this.authSubscription = this.authService.getSubjectCurrentUser().subscribe(
      user => {
        this.user = user
      }
    );
  }

  saveChanges() {
    this.updateProfileSubscription = this.userService
      .updateProfileData(
        this.userService.createUserDetails(
          this.user.email,
          this.user.name,
          this.user.surname,
          this.user.phoneNumber,
          this.user.city
        )
      )
      .subscribe(
        res => {
          const parsedUser = res as User;
          this.authService.setUserInLocalStorage(parsedUser);
          this.router.navigate(['/serb-uber/user/profile-page']);
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
    this.router.navigate(['/serb-uber/user/profile-page']);
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
  }
}