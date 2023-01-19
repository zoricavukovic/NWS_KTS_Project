import { Component, Input, OnChanges, OnInit } from '@angular/core';
import Chart from 'chart.js/auto';
import { ChartItem } from 'src/modules/shared/models/chart/chart-data';
import { ConfigService } from 'src/modules/shared/services/config-service/config.service';
import zoomPlugin from 'chartjs-plugin-zoom';

@Component({
  selector: 'app-report-graph',
  templateUrl: './report-graph.component.html',
  styleUrls: ['./report-graph.component.css']
})
export class ReportGraphComponent implements OnInit, OnChanges {

  @Input() chartItems: ChartItem[];

  @Input() chartType: string;

  @Input() isRegular: boolean;

  @Input() dateRange: string;

  chart: Chart;
  label: string;

  constructor(
    private configService: ConfigService
  ) {
    this.chartItems = [];
    this.chartType = '';
    this.dateRange = '';
    this.chart = null;
  }

  ngOnInit(): void {
    if (this.chartItems && this.chartType && this.dateRange) {
      this.setLabel();
      this.chart = new Chart("chart", {
        data: {
          labels: this.chartItems.map(row => row.date),
          datasets: [
            {
              label: this.label,
              data: this.chartItems.map(row => row.value),
              backgroundColor: 'rgb(216, 180, 137, 0.6)'
            }
          ]
      },
        type: 'bar',
        options: {
          plugins: {
            zoom: {
              zoom: {
                wheel: {
                  enabled: true
                }
              }
            },
            title: {
              display: true,
              font: {
                size: 20
              },
              text: `Report for a period ${this.dateRange}`
            }
          }
        }
      });
      Chart.register(zoomPlugin);
    }
  }

  ngOnChanges() {
    if (this.chartItems && this.chartType && this.dateRange && this.chart) {
      this.chart.data.datasets[0].data = this.chartItems.map(row => row.value);
      this.chart.options.plugins.title.text = `Report for a period ${this.dateRange}`;
      this.chart.data.labels = this.chartItems.map(row => row.date);
      this.chart.update();
    }
  }

  setLabel() {
    if (this.chartType === this.configService.SELECTED_SPENDING_REPORT) {
      this.label = this.isRegular ? 'Tokens spent per day' : 'Tokens earned per day';
    } else if (this.chartType === this.configService.SELECTED_DISTANCE_REPORT) {
      this.label = 'Distance in km per day';
    } else if (this.chartType === this.configService.SELECTED_RIDES_REPORT) {
      this.label = 'Number of rides per day';
    }
  }

}
