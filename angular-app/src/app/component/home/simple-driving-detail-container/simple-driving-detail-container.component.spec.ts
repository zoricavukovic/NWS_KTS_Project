import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SimpleDrivingDetailContainerComponent } from './simple-driving-detail-container.component';

describe('SimpleDrivingDetailContainerComponent', () => {
  let component: SimpleDrivingDetailContainerComponent;
  let fixture: ComponentFixture<SimpleDrivingDetailContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SimpleDrivingDetailContainerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SimpleDrivingDetailContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
