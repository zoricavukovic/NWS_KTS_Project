import { Component, Input, OnInit } from '@angular/core';
import { User } from 'src/modules/shared/models/user/user';

@Component({
  selector: 'app-admin-home',
  templateUrl: './admin-home.component.html',
  styleUrls: ['./admin-home.component.css']
})
export class AdminHomeComponent implements OnInit {

  @Input() currentUser: User;

  constructor() { }

  ngOnInit(): void {
  }

}
