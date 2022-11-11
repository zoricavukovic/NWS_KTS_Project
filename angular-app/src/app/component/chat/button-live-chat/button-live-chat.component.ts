import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { User } from 'src/app/model/response/user/user';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-button-live-chat',
  templateUrl: './button-live-chat.component.html',
  styleUrls: ['./button-live-chat.component.css']
})
export class ButtonLiveChatComponent implements OnInit, OnDestroy {

  isAdmin: boolean;
  loggedUser: User;

  authSubscription: Subscription;

  constructor(private authService: AuthService) { }
  
  ngOnInit(): void {
    this.authSubscription = this.authService.getCurrentUser().subscribe(
        user => {
          this.loggedUser = user;
          this.isAdmin = this.authService.userIsAdmin();
        }
      );
    }
    
    isLoggedIn(): boolean {
      
      return this.loggedUser !== null;
    }
    
    ngOnDestroy(): void {
      if (this.authSubscription) {
        this.authSubscription.unsubscribe();
      }
    }

}
