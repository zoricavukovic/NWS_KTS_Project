import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { RegularUserService } from 'src/app/service/regular-user.service';
import { User } from 'src/app/model/user/user';
import { RegularUser } from 'src/app/model/user/regular-user';

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
