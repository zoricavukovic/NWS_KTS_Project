import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewAndReportTabsComponent } from './review-and-report-tabs.component';

describe('ReviewAndReportTabsComponent', () => {
  let component: ReviewAndReportTabsComponent;
  let fixture: ComponentFixture<ReviewAndReportTabsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewAndReportTabsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewAndReportTabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
