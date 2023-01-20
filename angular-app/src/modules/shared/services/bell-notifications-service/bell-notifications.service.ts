import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { BellNotification } from '../../models/notification/bell-notification';
import { ConfigService } from '../config-service/config.service';
import { GenericService } from '../generic-service/generic.service';

@Injectable({
  providedIn: 'root'
})
export class BellNotificationsService extends GenericService<BellNotification> {
  
  bell$: BehaviorSubject<BellNotification[]>;
  
  constructor(
    private http: HttpClient,
    private configService: ConfigService
    ) {
      super(http, configService.NOTIFICATION_BELL_URL);
      this.bell$ = new BehaviorSubject<BellNotification[]>([]);
    }
    
    getBellNotifications(userId: number): BehaviorSubject<BellNotification[]> {
      this.http
      .get<BellNotification[]>(this.configService.bellNotificationsForUser(userId))
      .subscribe(res => {
        this.bell$.next(res);
      });
      
      return this.bell$;
    }
    
    setAllAsSeen(userId: number): Observable<boolean> {
      return this.http
      .put<boolean>(this.configService.setAllNotificationsAsSeen(userId), null)
    }
    
    getNumOfNotifications(): number {
      let numOfNotifications = 0;
      for (const notification of this.bell$.value) {
        if (!notification.seen) {
          numOfNotifications += 1;
        }
      }
      
      return numOfNotifications;
    }
    
    addNotification(bellNotification: BellNotification): void {
      const copyNotifications: BellNotification[] = this.bell$.value;
      this.bell$.next([bellNotification, ...copyNotifications]);
    }

    resetBell(): void {
      this.bell$.next([]);
    }
  }
  