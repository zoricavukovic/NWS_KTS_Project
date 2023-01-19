import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DriverUpdateApproval } from 'src/modules/shared/models/user/update-approval';
import { ConfigService } from 'src/modules/shared/services/config-service/config.service';
import { GenericService } from 'src/modules/shared/services/generic-service/generic.service';

@Injectable({
  providedIn: 'root'
})
export class DriverUpdateApprovalService  extends GenericService<DriverUpdateApproval> {

  constructor(
    private http: HttpClient,
    private configService: ConfigService,
  ) {
    super(http, configService.DRIVER_UPDATE_APPROVAL);
  }

  reject(id: number): Observable<boolean> {

    return this.http.put<boolean>(this.configService.rejectDriverRequest(id), null);
  }

  approve(id: number): Observable<boolean> {

    return this.http.put<boolean>(this.configService.approveDriverRequest(id), null);
  }

}
