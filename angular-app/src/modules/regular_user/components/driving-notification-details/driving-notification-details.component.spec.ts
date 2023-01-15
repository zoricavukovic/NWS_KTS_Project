import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingNotificationDetailsComponent } from './driving-notification-details.component';

describe('DrivingNotificationDetailsComponent', () => {
  let component: DrivingNotificationDetailsComponent;
  let fixture: ComponentFixture<DrivingNotificationDetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivingNotificationDetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivingNotificationDetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
