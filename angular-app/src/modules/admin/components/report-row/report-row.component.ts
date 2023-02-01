import { Component, Input } from '@angular/core';
import { Router } from '@angular/router';
import { Report } from 'src/modules/shared/models/report/report';
import { ConfigService } from 'src/modules/shared/services/config-service/config.service';

@Component({
  selector: 'app-report-row',
  templateUrl: './report-row.component.html',
  styleUrls: ['./report-row.component.scss']
})
export class ReportRowComponent {

  @Input() report: Report;

  @Input() numOfReports: number;

  constructor(
    private configService: ConfigService,
    private router: Router
  ) {
    this.report = null;
    this.numOfReports = 0;
  }

  goToUserProfile(id: number): void {
    this.router.navigateByUrl('/', {skipLocationChange: true}).then(()=>
    this.router.navigate([`/serb-uber/user/user-profile/${id}`]));
  }
}
