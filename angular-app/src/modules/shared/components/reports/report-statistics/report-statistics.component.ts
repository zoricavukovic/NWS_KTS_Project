import { Component, Input } from '@angular/core';

@Component({
  selector: 'app-report-statistics',
  templateUrl: './report-statistics.component.html',
  styleUrls: ['./report-statistics.component.css']
})
export class ReportStatisticsComponent {

  @Input() sum: number;

  @Input() average: number;

  constructor() {
    this.sum = 0;
    this.average = 0;
  }

  getMaxValueTotal(): number {

    return (this.sum === 0) ? 1 : this.sum;
  }

  getMaxValueAverage(): number {

    return (this.average === 0) ? 1 : this.average;
  }

}
