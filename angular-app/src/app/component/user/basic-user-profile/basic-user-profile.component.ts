import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { User } from 'src/app/model/user/user';
import { UserService } from 'src/app/service/user.service';
import { ToastrService } from 'ngx-toastr';
import { Subscription } from 'rxjs';


@Component({
  selector: 'app-basic-user-profile',
  templateUrl: './basic-user-profile.component.html',
  styleUrls: ['./basic-user-profile.component.css']
})
export class BasicUserProfileComponent implements OnInit, OnDestroy {

  userId: string = "";
  userRole: string = "";
  user: User = null;

  userSubscription: Subscription;

  ROLE_DRIVER: string = "ROLE_DRIVER";
  ROLE_REGULAR: string = "ROLE_REGULAR_USER";

  constructor(
    private route: ActivatedRoute,
    private userService: UserService,
    private toast: ToastrService,  
  ) { }

  ngOnInit(): void {
    this.userId = this.route.snapshot.paramMap.get('id');
    this.userRole = this.route.snapshot.paramMap.get('role');
    this.userSubscription = this.userService.getUser(this.userId).subscribe(
      (user: User) => {
        this.user = user
      },
      error => {
        this.toast.error(`User with id ${this.userId} not found.`, 'User not found');
      }
    );
  }

  userIsDriver(): boolean {
    
    return this.user?.role.name === this.ROLE_DRIVER;
  }

  checkIfRoleIsDriverOrRegular(): boolean {

    return this.userRole === this.ROLE_DRIVER || this.userRole === this.ROLE_REGULAR
  }

  ngOnDestroy(): void {
    if (this.userSubscription) {
      this.userSubscription.unsubscribe();
    }
  }

}
