import { TestBed } from '@angular/core/testing';

import { TokenBankService } from './token-bank.service';

describe('TokenBankService', () => {
  let service: TokenBankService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TokenBankService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
