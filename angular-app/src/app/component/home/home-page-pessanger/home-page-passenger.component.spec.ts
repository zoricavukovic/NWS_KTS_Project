import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePagePassengerComponent } from './home-page-passenger.component';

describe('HomePagePessangerComponent', () => {
  let component: HomePagePassengerComponent;
  let fixture: ComponentFixture<HomePagePassengerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomePagePassengerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomePagePassengerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
