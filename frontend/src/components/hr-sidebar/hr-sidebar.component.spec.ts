import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HrSidebarComponent } from './hr-sidebar.component';

describe('HrSidebarComponent', () => {
  let component: HrSidebarComponent;
  let fixture: ComponentFixture<HrSidebarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HrSidebarComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(HrSidebarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
