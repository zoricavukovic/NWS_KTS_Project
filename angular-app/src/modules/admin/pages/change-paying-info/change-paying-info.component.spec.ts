import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ChangePayingInfoComponent } from './change-paying-info.component';

describe('ChangePayingInfoComponent', () => {
  let component: ChangePayingInfoComponent;
  let fixture: ComponentFixture<ChangePayingInfoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ChangePayingInfoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ChangePayingInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
