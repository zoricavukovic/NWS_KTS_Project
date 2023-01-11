import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmBlockingDialogComponent } from './confirm-blocking-dialog.component';

describe('ConfirmBlockingDialogComponent', () => {
  let component: ConfirmBlockingDialogComponent;
  let fixture: ComponentFixture<ConfirmBlockingDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ConfirmBlockingDialogComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ConfirmBlockingDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
