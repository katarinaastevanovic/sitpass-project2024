import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReserveExcerciseComponent } from './reserve-excercise.component';

describe('ReserveExcerciseComponent', () => {
  let component: ReserveExcerciseComponent;
  let fixture: ComponentFixture<ReserveExcerciseComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReserveExcerciseComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReserveExcerciseComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
