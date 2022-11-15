import { Component, Input, OnInit } from '@angular/core';
import { PossibleRoute } from '../../model/response/possible-routes';

@Component({
  selector: 'route-row',
  templateUrl: './route-row.component.html',
  styleUrls: ['./route-row.component.css'],
})
export class RouteRowComponent implements OnInit {
  @Input() route: PossibleRoute;
  @Input() index: number;
  color: string;
  rgbDeepBlue: number[] = [44, 75, 97];

  ngOnInit(): void {
    this.color =
      'rgb(' +
      this.incrementShadeOfColor(this.index, 0) +
      ', ' +
      this.incrementShadeOfColor(this.index, 1) +
      ', ' +
      this.incrementShadeOfColor(this.index, 2) +
      ')';
  }

  private incrementShadeOfColor(index: number, number: number) {
    return this.rgbDeepBlue[number] + index;
  }
}
