import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MyExcercisesComponent } from './my-excercises.component';

describe('MyExcercisesComponent', () => {
  let component: MyExcercisesComponent;
  let fixture: ComponentFixture<MyExcercisesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MyExcercisesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(MyExcercisesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
