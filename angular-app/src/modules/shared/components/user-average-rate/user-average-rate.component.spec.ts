import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserAverageRateComponent } from './user-average-rate.component';

describe('UserAverageRateComponent', () => {
  let component: UserAverageRateComponent;
  let fixture: ComponentFixture<UserAverageRateComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserAverageRateComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserAverageRateComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
