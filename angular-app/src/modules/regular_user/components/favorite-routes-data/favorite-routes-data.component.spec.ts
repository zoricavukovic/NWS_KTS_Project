import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FavoriteRoutesDataComponent } from './favorite-routes-data.component';

describe('FavoriteRoutesDataComponent', () => {
  let component: FavoriteRoutesDataComponent;
  let fixture: ComponentFixture<FavoriteRoutesDataComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FavoriteRoutesDataComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FavoriteRoutesDataComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
