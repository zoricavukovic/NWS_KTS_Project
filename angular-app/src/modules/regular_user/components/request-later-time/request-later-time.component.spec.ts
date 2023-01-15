import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RequestLaterTimeComponent } from './request-later-time.component';

describe('RequestLaterTimeComponent', () => {
  let component: RequestLaterTimeComponent;
  let fixture: ComponentFixture<RequestLaterTimeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RequestLaterTimeComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RequestLaterTimeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
