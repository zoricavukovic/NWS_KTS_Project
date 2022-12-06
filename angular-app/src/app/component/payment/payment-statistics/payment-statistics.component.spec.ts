import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PaymentStatisticsComponent } from './payment-statistics.component';

describe('PaymentStatisticsComponent', () => {
  let component: PaymentStatisticsComponent;
  let fixture: ComponentFixture<PaymentStatisticsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PaymentStatisticsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaymentStatisticsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
