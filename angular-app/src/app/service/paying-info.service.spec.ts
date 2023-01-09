import { TestBed } from '@angular/core/testing';

import { PayingInfoService } from './paying-info.service';

describe('PayingInfoService', () => {
  let service: PayingInfoService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PayingInfoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
