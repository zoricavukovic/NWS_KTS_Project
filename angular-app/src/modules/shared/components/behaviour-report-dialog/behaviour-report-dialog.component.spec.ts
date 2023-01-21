import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BehaviourReportDialogComponent } from './behaviour-report-dialog.component';

describe('BehaviourReportDialogComponent', () => {
  let component: BehaviourReportDialogComponent;
  let fixture: ComponentFixture<BehaviourReportDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ BehaviourReportDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BehaviourReportDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
