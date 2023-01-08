import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RoutesOptionComponent } from './routes-option.component';

describe('RoutesOptionComponent', () => {
  let component: RoutesOptionComponent;
  let fixture: ComponentFixture<RoutesOptionComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ RoutesOptionComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RoutesOptionComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
