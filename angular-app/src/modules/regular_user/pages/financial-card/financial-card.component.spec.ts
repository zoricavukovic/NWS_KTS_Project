import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FinancialCardComponent } from './financial-card.component';

describe('FinancialCardComponent', () => {
  let component: FinancialCardComponent;
  let fixture: ComponentFixture<FinancialCardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FinancialCardComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FinancialCardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
