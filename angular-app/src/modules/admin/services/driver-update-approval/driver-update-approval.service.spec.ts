import { TestBed } from '@angular/core/testing';

import { DriverUpdateApprovalService } from './driver-update-approval.service';

describe('DriverUpdateApprovalService', () => {
  let service: DriverUpdateApprovalService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DriverUpdateApprovalService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
