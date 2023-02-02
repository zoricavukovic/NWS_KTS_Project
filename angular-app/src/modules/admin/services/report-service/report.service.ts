import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import {BehaviourReportRequest, Report} from "../../../shared/models/report/report";
import {ConfigService} from "../../../shared/services/config-service/config.service";

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(private http: HttpClient, private configService: ConfigService) { }

  getReports(id: number): Observable<Report[]> {

    return this.http.get<Report[]>(this.configService.reportsForUserUrl(id));
  }

  createReport(senderId: number, receiverId: number, message: string): Observable<boolean> {

    return this.http.post<boolean>(this.configService.reportUrlForUser(), 
      this.createBehaviourReportRequest(senderId, receiverId, message));
  }

  createReportDriver(senderId: number, drivingId: number, message: string): Observable<boolean> {

    return this.http.post<boolean>(this.configService.reportUrlForDriver(), 
      this.createBehaviourDriverReportRequest(senderId, drivingId, message));
  }
  
  createBehaviourReportRequest(senderId: number, receiverId: number, message: string): BehaviourReportRequest {
   
    return {
      senderId: senderId,
      receiverId: receiverId,
      message: message
    }
  }

  createBehaviourDriverReportRequest(senderId: number, drivingId: number, message: string): BehaviourReportRequest {
   
    return {
      senderId: senderId,
      drivingId: drivingId,
      message: message
    }
  }
}

