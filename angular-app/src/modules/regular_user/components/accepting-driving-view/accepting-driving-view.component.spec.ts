import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AcceptingDrivingViewComponent } from './accepting-driving-view.component';

describe('AcceptingDrivingViewComponent', () => {
  let component: AcceptingDrivingViewComponent;
  let fixture: ComponentFixture<AcceptingDrivingViewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AcceptingDrivingViewComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AcceptingDrivingViewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
