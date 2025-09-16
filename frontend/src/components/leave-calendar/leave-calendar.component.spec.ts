import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LeaveCalendarComponent } from './leave-calendar.component';

describe('LeaveCalendarComponent', () => {
  let component: LeaveCalendarComponent;
  let fixture: ComponentFixture<LeaveCalendarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LeaveCalendarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(LeaveCalendarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
