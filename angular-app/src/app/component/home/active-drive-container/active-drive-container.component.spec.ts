import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActiveDriveContainerComponent } from './active-drive-container.component';

describe('ActiveDriveContainerComponent', () => {
  let component: ActiveDriveContainerComponent;
  let fixture: ComponentFixture<ActiveDriveContainerComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ActiveDriveContainerComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ActiveDriveContainerComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
