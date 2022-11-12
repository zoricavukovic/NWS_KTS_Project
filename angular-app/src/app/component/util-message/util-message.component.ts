import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'util-message',
  templateUrl: './util-message.component.html',
  styleUrls: ['./util-message.component.css']
})
export class UtilMessageComponent implements OnInit {
  @Input() message: string;

  constructor() { }

  ngOnInit(): void {
  }

}
