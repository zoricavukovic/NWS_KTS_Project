import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ApprovalRowComponent } from './approval-row.component';

describe('ApprovalRowComponent', () => {
  let component: ApprovalRowComponent;
  let fixture: ComponentFixture<ApprovalRowComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ApprovalRowComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ApprovalRowComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
