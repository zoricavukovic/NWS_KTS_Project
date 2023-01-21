import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RateDrivingOrSkipComponent } from './rate-driving-or-skip.component';

describe('RateDrivingOrSkipComponent', () => {
  let component: RateDrivingOrSkipComponent;
  let fixture: ComponentFixture<RateDrivingOrSkipComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RateDrivingOrSkipComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RateDrivingOrSkipComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
