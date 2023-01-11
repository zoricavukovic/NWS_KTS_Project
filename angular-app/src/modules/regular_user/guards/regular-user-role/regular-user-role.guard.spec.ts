import { TestBed } from '@angular/core/testing';

import { RegularUserRoleGuard } from './regular-user-role.guard';

describe('RegularUserRoleGuard', () => {
  let guard: RegularUserRoleGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(RegularUserRoleGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
