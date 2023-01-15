import { ComponentFixture, TestBed } from '@angular/core/testing';

import { VehicleRateDataComponent } from './vehicle-rate-data.component';

describe('VehicleRateDataComponent', () => {
  let component: VehicleRateDataComponent;
  let fixture: ComponentFixture<VehicleRateDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ VehicleRateDataComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(VehicleRateDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
