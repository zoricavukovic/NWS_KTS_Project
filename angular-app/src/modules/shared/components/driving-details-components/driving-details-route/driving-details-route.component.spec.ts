import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingDetailsRouteComponent } from './driving-details-route.component';

describe('DrivingRouteComponent', () => {
  let component: DrivingDetailsRouteComponent;
  let fixture: ComponentFixture<DrivingDetailsRouteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DrivingDetailsRouteComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(DrivingDetailsRouteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
