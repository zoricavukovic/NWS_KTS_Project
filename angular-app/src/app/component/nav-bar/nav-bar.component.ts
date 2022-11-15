import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/response/user/user';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit, OnDestroy {

  currentUser: User;
  isAdmin: boolean;
  isRegularUser: boolean;
  currentUserSubscription: Subscription;
  logoutSubscription: Subscription;
  
  constructor(
    private authService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.currentUserSubscription = this.authService.getCurrentUser().subscribe((data) => {
      this.currentUser=data; 
      this.isAdmin = this.authService.userIsAdmin();
      this.isRegularUser = this.authService.userIsRegular(this.currentUser);
      this.isAdmin = this.authService.userIsAdmin(this.currentUser);
    });
  }

  redirectToEditPage() {
    this.router.navigate(['/edit-profile-data']);
  }

  redirectToProfilePage() {
    this.router.navigate(['/profile-page']);
  }

  redirectToMessagesPage() {
    this.router.navigate(['/messages']);
  }

  logOut(){
    this.logoutSubscription = this.authService.setOfflineStatus(this.currentUser).subscribe((res) => {
      console.log("Logut");
    })
    this.authService.logOut();
    this.router.navigate(['/home-page']);
  }

  ngOnDestroy(): void {
    this.currentUserSubscription.unsubscribe();
  }
}
