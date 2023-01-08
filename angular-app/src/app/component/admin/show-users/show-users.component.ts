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
  pageSize = 6;
  totalPages:number;
  currentPage = 0;
  usersSubscription: Subscription;
  constructor(private regularUserService: RegularUserService) {}

  ngOnInit(): void {
    this.usersSubscription = this.regularUserService
      .getWithPagination(this.currentPage, this.pageSize)
      .subscribe(regularUsersResponse => {
        this.regularUsers = regularUsersResponse;
        this.totalPages = this.regularUsers[0].pageNumber;
      });
  }

  changePage(newPage: number) {
    this.currentPage = newPage - 1;
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
