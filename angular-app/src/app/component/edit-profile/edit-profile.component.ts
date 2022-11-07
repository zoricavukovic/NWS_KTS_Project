import { Component, Inject, Input, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { MyErrorStateMatcher } from '../registration/registration.component';
import { map, Observable, startWith, Subscription } from 'rxjs';
import { UserService } from 'src/app/service/user.service';
import { User } from 'src/app/model/user';
import { isFormValid } from 'src/app/util/validation-function';
import { UsersProfileUpdateRequest } from 'src/app/model/users-profile-update-request';
import { AuthService } from 'src/app/service/auth.service';
import { Router } from '@angular/router';


@Component({
  selector: 'app-edit-profile',
  templateUrl: './edit-profile.component.html',
  styleUrls: ['./edit-profile.component.css']
})
export class EditProfileComponent implements OnInit, OnDestroy {

  user: User;

  authSubrscription: Subscription;

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
    private router: Router
  ) {
    this.filteredCities = this.editDataForm.get('cityFormControl').valueChanges.pipe(
      startWith(''),
      map(city=> (city ? this._filterCities(city) : this.cities.slice())),
    );
  }
  
  ngOnInit(): void {
      this.authSubrscription = this.authService.getCurrentUser().subscribe(
        user => this.user = user
      );    
  }
    
  ngOnDestroy(): void {
    if (this.authSubrscription) {
      this.authSubrscription.unsubscribe();
    }
  }
  saveChanges() {
    this.userService.updateProfileData(
      new UsersProfileUpdateRequest(
        this.user.email,
        this.user.name,
        this.user.surname,
        this.user.phoneNumber,
        this.user.city
      )
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

}
