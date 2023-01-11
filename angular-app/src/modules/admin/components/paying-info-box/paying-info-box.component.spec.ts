import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PayingInfoBoxComponent } from './paying-info-box.component';

describe('PayingInfoBoxComponent', () => {
  let component: PayingInfoBoxComponent;
  let fixture: ComponentFixture<PayingInfoBoxComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PayingInfoBoxComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PayingInfoBoxComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
