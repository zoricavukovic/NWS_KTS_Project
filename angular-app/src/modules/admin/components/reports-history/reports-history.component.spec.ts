import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReportsHistoryComponent } from './reports-history.component';

describe('ReportsHistoryComponent', () => {
  let component: ReportsHistoryComponent;
  let fixture: ComponentFixture<ReportsHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReportsHistoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReportsHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
