import { Component, Input, OnInit, Output } from '@angular/core';
import { PossibleRoute } from 'src/app/model/route/possible-routes';
import { EventEmitter } from '@angular/core';
@Component({
  selector: 'route-row',
  templateUrl: './route-row.component.html',
  styleUrls: ['./route-row.component.css'],
})
export class RouteRowComponent implements OnInit {
  @Input() route: PossibleRoute;
  @Output() chosenRouteEvent = new EventEmitter<string>();
  color: string;
  rgbDeepBlue: number[] = [44, 75, 97];

  ngOnInit(): void {
    this.color =
      'rgb(' +
      this.incrementShadeOfColor(0, 0) +
      ', ' +
      this.incrementShadeOfColor(0, 1) +
      ', ' +
      this.incrementShadeOfColor(0, 2) +
      ')';
  }

  chooseRoute() {
    this.chosenRouteEvent.emit('changed');
  }

  private incrementShadeOfColor(index: number, number: number) {
    return this.rgbDeepBlue[number] + index;
  }
}
