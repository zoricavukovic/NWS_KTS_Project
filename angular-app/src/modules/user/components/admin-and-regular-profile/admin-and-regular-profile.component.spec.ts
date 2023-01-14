import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminAndRegularProfileComponent } from './admin-and-regular-profile.component';

describe('AdminAndRegularProfileComponent', () => {
  let component: AdminAndRegularProfileComponent;
  let fixture: ComponentFixture<AdminAndRegularProfileComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ AdminAndRegularProfileComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminAndRegularProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
