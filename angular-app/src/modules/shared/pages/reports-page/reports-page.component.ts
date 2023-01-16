import { Component } from '@angular/core';
import { ConfigService } from '../../services/config-service/config.service';

@Component({
  selector: 'app-reports-page',
  templateUrl: './reports-page.component.html',
  styleUrls: ['./reports-page.component.css']
})
export class ReportsPageComponent {

  constructor(
    public configService: ConfigService
  ) {
  }

}
