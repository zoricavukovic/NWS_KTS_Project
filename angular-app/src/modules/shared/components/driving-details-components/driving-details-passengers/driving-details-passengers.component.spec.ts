import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingDetailsPassengersComponent } from './driving-details-passengers.component';

describe('DrivingDetailsPassengersComponent', () => {
  let component: DrivingDetailsPassengersComponent;
  let fixture: ComponentFixture<DrivingDetailsPassengersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivingDetailsPassengersComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivingDetailsPassengersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
