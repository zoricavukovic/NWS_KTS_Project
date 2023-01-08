import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WaitingForAcceptRideContainerComponent } from './waiting-for-accept-ride-container.component';

describe('WaitingForAcceptRideContainerComponent', () => {
  let component: WaitingForAcceptRideContainerComponent;
  let fixture: ComponentFixture<WaitingForAcceptRideContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ WaitingForAcceptRideContainerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WaitingForAcceptRideContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
