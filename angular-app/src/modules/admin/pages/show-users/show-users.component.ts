import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import {RegularUser} from "../../../shared/models/user/regular-user";
import {RegularUserService} from "../../../shared/services/regular-user-service/regular-user.service";

@Component({
  selector: 'app-show-users',
  templateUrl: './show-users.component.html',
  styleUrls: ['./show-users.component.css'],
})
export class ShowUsersComponent implements OnInit, OnDestroy {
  regularUsers: RegularUser[];

  usersSubscription: Subscription;
  constructor(private regularUserService: RegularUserService) {}

  ngOnInit(): void {
    this.usersSubscription = this.regularUserService
      .getAll()
      .subscribe(regularUsersResponse => {
        this.regularUsers = regularUsersResponse;
      });
  }

  ngOnDestroy(): void {
    if (this.usersSubscription) {
      this.usersSubscription.unsubscribe();
    }
  }
}
