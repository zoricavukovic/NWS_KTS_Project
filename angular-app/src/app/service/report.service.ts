import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Report } from '../model/report/report';
import { ConfigService } from './config.service';

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient, private configService: ConfigService) { }

  getReports(id: number): Observable<Report[]> {

    return this.http.get<Report[]>(this.configService.get_reports_for_user(id));
  }
}
