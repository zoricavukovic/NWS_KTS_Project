import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-successfull-verification',
  templateUrl: './successfull-verification.component.html',
  styleUrls: ['./successfull-verification.component.css']
})
export class SuccessfullVerificationComponent implements OnInit {

  constructor(private router: Router) { }

  ngOnInit(): void {
  }

  redirectToLogin() {
    this.router.navigate(['/login']);
  }

}
