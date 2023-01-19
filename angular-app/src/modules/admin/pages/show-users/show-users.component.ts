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
  pageSize = 6;
  totalPages:number;
  currentPage = 0;

  usersSubscription: Subscription;
  constructor(private regularUserService: RegularUserService) {
    this.regularUsers = [];
  }

  ngOnInit(): void {
    this.usersSubscription = this.regularUserService
    .getWithPagination(this.currentPage, this.pageSize)
      .subscribe(regularUsersResponse => {
        this.regularUsers = regularUsersResponse;
        this.totalPages = this.regularUsers[0].pageNumber;
      });
  }

  changePage(newPage: number) {
    this.currentPage = newPage;
    this.regularUserService
      .getWithPagination(this.currentPage, this.pageSize)
      .subscribe((response: RegularUser[]) => {
        this.regularUsers = response;
        if(this.regularUsers.length > 0){
          this.totalPages = this.regularUsers[0].pageNumber;
        }
      });
  }

  ngOnDestroy(): void {
    if (this.usersSubscription) {
      this.usersSubscription.unsubscribe();
    }
  }
}
