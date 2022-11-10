import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { UserService } from 'src/app/service/user.service';
import { User } from 'src/app/model/response/user/user';

@Component({
  selector: 'app-show-users',
  templateUrl: './show-users.component.html',
  styleUrls: ['./show-users.component.css']
})
export class ShowUsersComponent implements OnInit {

  users: User[];
  
  usersSubscription: Subscription;
  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.usersSubscription = this.userService.getAllRegularUsers().subscribe(
      response => {this.users = response; console.log(this.users)}
    )
  }

}
