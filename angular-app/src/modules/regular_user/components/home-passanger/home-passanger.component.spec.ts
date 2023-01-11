import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HomePassangerComponent } from './home-passanger.component';

describe('HomePagePessangerComponent', () => {
  let component: HomePassangerComponent;
  let fixture: ComponentFixture<HomePassangerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ HomePassangerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HomePassangerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
