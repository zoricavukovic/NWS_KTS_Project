import { Component, Inject, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MyErrorStateMatcher } from '../registration/registration.component';
import { map, Observable, startWith, Subscription } from 'rxjs';
import { UserService } from 'src/app/service/user.service';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';
import { User } from 'src/app/model/user';
import { isFormValid } from 'src/app/util/validation-function';
import { UsersProfileUpdateRequest } from 'src/app/model/users-profile-update-request';


@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit {

  userCopy: User = Object.assign({}, this.user)

  editDataForm = new FormGroup({
    'phoneNumberFormControl' : new FormControl('', [Validators.required, Validators.pattern("[0-9]*")]),
    'nameFormControl' : new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
    'surnameFormControl' : new FormControl('', [Validators.required, Validators.pattern('[a-zA-Z ]*')]),
    'cityFormControl' : new FormControl('',[Validators.required]),
  });

  matcher = new MyErrorStateMatcher();
  cities: string[] = ['Belgrade', 'Novi Sad', 'Kraljevo', 'Sabac'];
  filteredCities: Observable<string[]>;

  constructor(
    private userService: UserService,
    @Inject(MAT_DIALOG_DATA) public user: User
  ) {
    this.filteredCities = this.editDataForm.get('cityFormControl').valueChanges.pipe(
      startWith(''),
      map(city=> (city ? this._filterCities(city) : this.cities.slice())),
    );
  }

  ngOnInit(): void {
  }

  saveChanges() {
    this.userService.updateProfileData(
      new UsersProfileUpdateRequest(
        this.userCopy.email,
        this.userCopy.name,
        this.userCopy.surname,
        this.userCopy.phoneNumber,
        this.userCopy.city
      )
    );
  }

  checkValidForm() {
    return !isFormValid(this.editDataForm);
  }

  _filterCities(value: string): string[] {
    const filterValue = value.toLowerCase();
    
    return this.cities.filter(city => city.toLowerCase().includes(filterValue));
  }

}
