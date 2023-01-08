import { TestBed } from '@angular/core/testing';

import { RegularUserService } from './regular-user.service';

describe('RegularUserService', () => {
  let service: RegularUserService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegularUserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
