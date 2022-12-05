import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DrivingDetailsDriverComponent } from './driving-details-driver.component';

describe('DrivingDetailsDriverComponent', () => {
  let component: DrivingDetailsDriverComponent;
  let fixture: ComponentFixture<DrivingDetailsDriverComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DrivingDetailsDriverComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DrivingDetailsDriverComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
