import { ComponentFixture, TestBed } from '@angular/core/testing';

import { EmployeeBenefitComponent } from './employee-benefit.component';

describe('EmployeeBenefitComponent', () => {
  let component: EmployeeBenefitComponent;
  let fixture: ComponentFixture<EmployeeBenefitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EmployeeBenefitComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(EmployeeBenefitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
