import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserProfileReviewsComponent } from './user-profile-reviews.component';

describe('UserProfileReviewsComponent', () => {
  let component: UserProfileReviewsComponent;
  let fixture: ComponentFixture<UserProfileReviewsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ UserProfileReviewsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserProfileReviewsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
