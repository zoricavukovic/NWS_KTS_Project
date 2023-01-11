import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReviewsHistoryComponent } from './reviews-history.component';

describe('ReviewsHistoryComponent', () => {
  let component: ReviewsHistoryComponent;
  let fixture: ComponentFixture<ReviewsHistoryComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ ReviewsHistoryComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReviewsHistoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
