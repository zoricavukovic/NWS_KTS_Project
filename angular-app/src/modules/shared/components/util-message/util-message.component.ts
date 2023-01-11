import { Component, Input } from '@angular/core';

@Component({
  selector: 'util-message',
  templateUrl: './util-message.component.html',
  styleUrls: ['./util-message.component.css']
})
export class UtilMessageComponent {
  @Input() message: string;
}
