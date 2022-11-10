import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MyErrorStateMatcher } from '../auth/registration/registration.component';
import { map, Observable, startWith, Subscription } from 'rxjs';
import { UserService } from 'src/app/service/user.service';
import { User } from 'src/app/model/response/user/user';
import { isFormValid } from 'src/app/util/validation-function';
import { UsersProfileUpdateRequest } from 'src/app/model/request/user/user-profile-update';
import { AuthService } from 'src/app/service/auth.service';
import { Router } from '@angular/router';
import {ToastrService} from "ngx-toastr";


@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit, OnDestroy {

  user: User;

  authSubscription: Subscription;
  updateProfileSubscription: Subscription;

  editDataForm = new FormGroup({
    'phoneNumberFormControl' : new FormControl('', [Validators.required, Validators.pattern("[0-9]*")]),
    'nameFormControl' : new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
    'surnameFormControl' : new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
    'cityFormControl' : new FormControl('',[Validators.required, Validators.pattern('[a-zA-Z ]*')]),
  });

  matcher = new MyErrorStateMatcher();
  cities: string[] = ['Belgrade', 'Novi Sad', 'Kraljevo', 'Sabac'];
  filteredCities: Observable<string[]>;

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private router: Router,
    private toast: ToastrService
  ) {
    this.filteredCities = this.editDataForm.get('cityFormControl').valueChanges.pipe(
      startWith(''),
      map(city=> (city ? this._filterCities(city) : this.cities.slice())),
    );
  }

  ngOnInit(): void {
      this.authSubscription = this.authService.getCurrentUser().subscribe(
        user => this.user = user
      );
  }

  saveChanges() {
    this.updateProfileSubscription = this.userService.updateProfileData(
      new UsersProfileUpdateRequest(
        this.user.email,
        this.user.name,
        this.user.surname,
        this.user.phoneNumber,
        this.user.city
      )
    ).subscribe(
      res => {
        const parsedUser = res as User;
        this.authService.setUserInLocalStorage(parsedUser);
        this.router.navigate(['/profile-page']);
        this.toast.success("Profile is updated successfully!", "Profile update completed")
      },
      error => this.toast.error(error.error, "Profile update failed")
    );
  }

  cancel() {
    this.router.navigate(['/profile-page']);
  }

  checkValidForm() {
    return !isFormValid(this.editDataForm);
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