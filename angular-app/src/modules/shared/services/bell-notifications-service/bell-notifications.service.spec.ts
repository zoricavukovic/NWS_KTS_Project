import { TestBed } from '@angular/core/testing';

import { BellNotificationsService } from './bell-notifications.service';

describe('BellNotificationsService', () => {
  let service: BellNotificationsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BellNotificationsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
