import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AccountRequestComponent } from './account-request.component';

describe('AccountRequestComponent', () => {
  let component: AccountRequestComponent;
  let fixture: ComponentFixture<AccountRequestComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AccountRequestComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AccountRequestComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
