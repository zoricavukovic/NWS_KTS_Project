import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EnterLocationsComponent } from './enter-locations.component';

describe('EnterLocationsComponent', () => {
  let component: EnterLocationsComponent;
  let fixture: ComponentFixture<EnterLocationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ EnterLocationsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(EnterLocationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
