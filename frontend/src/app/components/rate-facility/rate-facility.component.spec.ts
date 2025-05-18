import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RateFacilityComponent } from './rate-facility.component';

describe('RateFacilityComponent', () => {
  let component: RateFacilityComponent;
  let fixture: ComponentFixture<RateFacilityComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RateFacilityComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RateFacilityComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
