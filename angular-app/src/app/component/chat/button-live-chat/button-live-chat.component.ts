import { Component } from '@angular/core';
import { AuthService } from 'src/app/service/auth.service';

@Component({
  selector: 'app-button-live-chat',
  templateUrl: './button-live-chat.component.html',
  styleUrls: ['./button-live-chat.component.css']
})
export class ButtonLiveChatComponent {

  showChatPoupup: boolean = false;

  constructor(public authService: AuthService) { }

  isLoggedIn(): boolean {

    return this.authService.getCurrentUser !== null;
  }

}
