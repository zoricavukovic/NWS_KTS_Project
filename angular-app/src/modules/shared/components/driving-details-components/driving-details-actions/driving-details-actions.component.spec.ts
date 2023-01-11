import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingDetailsActionsComponent } from './driving-details-actions.component';

describe('DrivingDetailsActionsComponent', () => {
  let component: DrivingDetailsActionsComponent;
  let fixture: ComponentFixture<DrivingDetailsActionsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivingDetailsActionsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivingDetailsActionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
