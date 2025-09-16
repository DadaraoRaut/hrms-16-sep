import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AttendanceregularizationComponent } from './attendanceregularization.component';

describe('AttendanceregularizationComponent', () => {
  let component: AttendanceregularizationComponent;
  let fixture: ComponentFixture<AttendanceregularizationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AttendanceregularizationComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AttendanceregularizationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
