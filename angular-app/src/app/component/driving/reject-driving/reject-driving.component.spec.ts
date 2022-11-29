import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RejectDrivingComponent } from './reject-driving.component';

describe('RejectDrivingComponent', () => {
  let component: RejectDrivingComponent;
  let fixture: ComponentFixture<RejectDrivingComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RejectDrivingComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RejectDrivingComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
